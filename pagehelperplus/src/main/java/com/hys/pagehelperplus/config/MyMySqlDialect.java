package com.hys.pagehelperplus.config;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.helper.MySqlDialect;
import com.hys.pagehelperplus.exception.ParseException;
import com.hys.pagehelperplus.util.PageHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义MySQL分页逻辑
 *
 * @author Robert Hou
 * @since 2020年11月27日 17:45
 **/
@Slf4j
public class MyMySqlDialect extends MySqlDialect {

    private static final Pattern PATTERN = Pattern.compile("SELECT\\s*([\\s|\\S]*?)\\s*?((FROM\\s*[0-9a-zA-Z_]*)\\s*[\\s|\\S]*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("SELECT\\s*([\\s|\\S]*?)\\s*?(((FROM\\s*[0-9a-zA-Z_]*)\\s*[\\s|\\S]*?)(ORDER\\s*BY\\s*[\\s|\\S]+))", Pattern.CASE_INSENSITIVE);
    private static final Pattern KEY_ALIAS_PATTERN = Pattern.compile("\\s*pageHelperAlias1.(\\S+)(\\s+(AS)?\\s*(\\S+))?", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTAINS_ORDER_PATTERN = Pattern.compile("[\\s|\\S]*ORDER\\s*BY[\\s|\\S]*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTAINS_JOIN_PATTERN = Pattern.compile("[\\s|\\S]*JOIN[\\s|\\S]*", Pattern.CASE_INSENSITIVE);

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        if (log.isDebugEnabled()) {
            log.debug("\n原始SQL：\n" + sql);
        }

        Matcher containsJoinMatcher = CONTAINS_JOIN_PATTERN.matcher(sql);
        if (containsJoinMatcher.find() || PageHelperUtils.getIsRelegated()) {
            //多表分页逻辑没实现，用默认的SQL后面追加limit子句的方式（对于不是JOIN方式来进行表连接的SQL（比如笛卡尔积），执行可能会报错。这个时候需要将降级选项手动置为true）
            PageHelperUtils.remove();
            return super.getPageSql(sql, page, pageKey);
        }

        List<String> keyNames = PageHelperUtils.getKeyNames();
        if (keyNames.size() == 0) {
            //没有添加@KeyNamesStrategy注解，也将表主键名设置为”id“
            PageHelperUtils.setKeyNames(new String[]{"id"});
            keyNames = PageHelperUtils.getKeyNames();
        }

        String fromTable = null;
        String fields = null;
        String afterClause = null;
        String orderByClause = null;
        boolean isSucceeded = false;

        Matcher m;
        boolean isOrderByContains = false;

        Matcher containsOrderMatcher = CONTAINS_ORDER_PATTERN.matcher(sql);
        if (containsOrderMatcher.find()) {
            isOrderByContains = true;
            m = ORDER_BY_PATTERN.matcher(sql);
        } else {
            m = PATTERN.matcher(sql);
        }

        Map<String, String> aliasMap = null;
        if (m.find()) {
            isSucceeded = true;

            //SELECT后面FROM前面的查找字段
            fields = m.group(1);

            if (fields != null) {
                int matchCount = 0;
                for (String keyName : keyNames) {
                    String regex = "[\\s|\\S]*" + keyName + "[\\s|,][\\s|\\S]*";
                    Pattern containsPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = containsPattern.matcher(fields);
                    if (matcher.find()) {
                        //只替换第一个是为了解决表主键起别名的情况
                        fields = fields.replaceFirst(keyName, "pageHelperAlias1." + keyName);
                        matchCount++;
                    }
                }

                String[] fieldsArray = fields.split(",");
                int initialCapacity = (int) (matchCount / 0.75 + 1);
                aliasMap = new HashMap<>(initialCapacity);
                for (String field : fieldsArray) {
                    field = field.trim();
                    Matcher matcher = KEY_ALIAS_PATTERN.matcher(field);
                    if (matcher.find()) {
                        String keyAlias = matcher.group(4);
                        if (keyAlias != null) {
                            String key1 = matcher.group(1);
                            aliasMap.put(key1, keyAlias);
                        }
                    }

                }
            }

            //FROM+后面的子句
            afterClause = isOrderByContains ? m.group(3) : m.group(2);
            if (page.getStartRow() == 0) {
                afterClause = afterClause + "\n LIMIT ? ";
            } else {
                afterClause = afterClause + "\n LIMIT ?, ? ";
            }

            //FROM+表名
            fromTable = isOrderByContains ? m.group(4) : m.group(3);

            if (isOrderByContains) {
                orderByClause = m.group(5);
            }
        }
        if (!isSucceeded) {
            throw new ParseException("解析失败！需要排查SQL！");
        }

        String returnSql = "SELECT " + fields + " " + fromTable + " pageHelperAlias1 \n" +
                " INNER JOIN ( SELECT " + getKeyNames(keyNames) + " " + afterClause + " ) pageHelperAlias2"
                + joinKeyNames(keyNames);
        if (orderByClause != null) {
            if (aliasMap != null) {
                for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
                    if (orderByClause.contains(entry.getKey()) && !orderByClause.contains(entry.getValue())) {
                        orderByClause = orderByClause.replace(entry.getKey(), entry.getValue());
                    }
                }
            }
            returnSql += " " + orderByClause;
        }
        if (log.isDebugEnabled()) {
            log.debug("\n拼接后的分页SQL：\n" + returnSql);
        }

        PageHelperUtils.remove();
        return returnSql;
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
}
