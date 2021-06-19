package com.hys.pagehelperplus.config;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.helper.MySqlDialect;
import com.hys.pagehelperplus.exception.ParseException;
import com.hys.pagehelperplus.util.PageHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 自定义MySQL分页逻辑
 *
 * @author Robert Hou
 * @since 2020年11月27日 17:45
 **/
@Slf4j
public class MyMySqlDialect extends MySqlDialect {

    private static final Pattern PATTERN = Pattern.compile("SELECT\\s*([\\s|\\S]*?)\\s*?((FROM\\s*[0-9a-zA-Z_`]*)\\s*[\\s|\\S]*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTAINS_JOIN_PATTERN = Pattern.compile("[\\s|\\S]*JOIN[\\s|\\S]*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTAINS_DISTINCT_PATTERN = Pattern.compile("\\s+DISTINCT\\s+", Pattern.CASE_INSENSITIVE);

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        Matcher containsJoinMatcher = CONTAINS_JOIN_PATTERN.matcher(sql);
        if (containsJoinMatcher.find() || PageHelperUtils.getIsRelegated()) {
            //多表分页逻辑没实现，用默认的SQL后面追加LIMIT子句的方式（对于不是JOIN方式来进行表连接的SQL（比如笛卡尔积），执行可能会报错。这个时候需要手动将降级选项置为true）
            log.info("使用了多表联查的查询SQL不会进行优化，转而使用默认的SQL后面追加LIMIT子句的方式");
            return invokeSuperMethod(sql, page, pageKey);
        }

        log.info("\n原始SQL：\n{}", sql);
        List<String> keyNames = PageHelperUtils.getKeyNames();
        if (keyNames.size() == 0) {
            //没有添加@KeyNamesStrategy注解，也将表主键名设置为”id“
            PageHelperUtils.setKeyNames(new String[]{"id"});
            keyNames = PageHelperUtils.getKeyNames();
        }

        String fromTable = null;
        String fields = null;
        String afterClause = null;
        boolean isSucceeded = false;

        Matcher m = PATTERN.matcher(sql);

        boolean isDistinctContains = false;
        if (m.find()) {
            isSucceeded = true;

            //SELECT后面FROM前面的查找字段
            fields = m.group(1);

            if (isOnlyContainsPrimaryKey(fields)) {
                //如果字段中只包含主键的话，则不需要优化（因为要查的字段都在B+树上，不需要回表进行查询）。改用默认的SQL后面追加LIMIT子句的方式
                log.info("要查询的字段中只包含主键，不需要优化，转而使用默认的SQL后面追加LIMIT子句的方式");
                return invokeSuperMethod(sql, page, pageKey);
            }

            if (fields != null) {
                //查看SQL中是否含有DISTINCT
                Matcher containsDistinctMatcher = CONTAINS_DISTINCT_PATTERN.matcher(sql);
                if (containsDistinctMatcher.find()) {
                    isDistinctContains = true;
                }

                for (String keyName : keyNames) {
                    String regex = "[\\s|\\S&&[^`]]*((`)?" + keyName + "(`)?)[\\s|,]?[\\s|\\S]*";
                    Pattern containsPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = containsPattern.matcher(fields);
                    if (matcher.find()) {
                        String keyNameAndBackQuoteIfContains = matcher.group(1);
                        //只替换第一个是为了解决表主键起别名的情况
                        fields = fields.replaceFirst(keyNameAndBackQuoteIfContains, "pageHelperAlias1." + keyNameAndBackQuoteIfContains);
                    }
                }
            }

            //FROM+后面的子句
            afterClause = m.group(2);
            if (page.getStartRow() == 0) {
                afterClause = afterClause + "\n LIMIT ? ";
            } else {
                afterClause = afterClause + "\n LIMIT ?, ? ";
            }

            //FROM+表名
            fromTable = m.group(3);
        }
        if (!isSucceeded) {
            throw new ParseException("解析失败！需要排查SQL！");
        }

        String returnSql = "SELECT " + fields + " " + fromTable + " pageHelperAlias1 \n" +
                " INNER JOIN ( SELECT " + (isDistinctContains ? "DISTINCT " : "") + getKeyNames(keyNames) + " " + afterClause + " ) pageHelperAlias2"
                + joinKeyNames(keyNames);
        log.info("\n拼接后的分页SQL：\n{}", returnSql);

        PageHelperUtils.remove();
        return returnSql;
    }

    private String invokeSuperMethod(String sql, Page page, CacheKey pageKey) {
        PageHelperUtils.remove();
        return super.getPageSql(sql, page, pageKey);
    }

    /**
     * KeyNames转换成String格式（逗号拼接）
     */
    private String getKeyNames(List<String> keyNames) {
        if (keyNames == null || keyNames.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String keyName : keyNames) {
            stringBuilder.append(keyName.trim()).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    /**
     * KeyNames转换成SQL JOIN的关联格式
     */
    private String joinKeyNames(List<String> keyNames) {
        if (keyNames == null || keyNames.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(" ON ");
        for (int i = 0; i < keyNames.size(); i++) {
            keyNames.set(i, keyNames.get(i).trim());
            stringBuilder.append("pageHelperAlias1.").append(keyNames.get(i)).append(" = pageHelperAlias2.").append(keyNames.get(i));
            if (i != keyNames.size() - 1) {
                stringBuilder.append(" AND ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 判断字段中是否只包含主键
     */
    private boolean isOnlyContainsPrimaryKey(String fields) {
        if (StringUtils.isBlank(fields)) {
            return false;
        }

        List<String> keyNames = PageHelperUtils.getKeyNames();
        if (keyNames == null || keyNames.isEmpty()) {
            return false;
        }
        //考虑keyNames有可能有重复主键名的情况 e.g.@KeyNamesStrategy(keyNames = {"order_id", "order_id"})
        List<String> uniqueKeyNames = unique(keyNames);
        String[] fieldArray = fields.split(",");
        List<String> uniqueFieldArray = unique(fieldArray);
        int length = 0;
        for (String field : uniqueFieldArray) {
            if (!uniqueKeyNames.contains(field)) {
                return false;
            }
            if (uniqueKeyNames.contains(field)) {
                length++;
            }
        }
        return length == uniqueKeyNames.size();
    }

    private String removeBackQuoteIfContains(String field) {
        if (StringUtils.isBlank(field)) {
            return StringUtils.EMPTY;
        }

        field = field.trim();
        if (field.startsWith("`") && field.endsWith("`")) {
            return field.substring(1, field.length() - 1);
        }
        return field;
    }

    private List<String> unique(List<String> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream().map(this::removeBackQuoteIfContains).distinct().collect(Collectors.toList());
    }

    private List<String> unique(String[] array) {
        if (array == null || array.length == 0) {
            return Collections.emptyList();
        }

        List<String> list = Arrays.stream(array).collect(Collectors.toList());
        return unique(list);
    }
}
