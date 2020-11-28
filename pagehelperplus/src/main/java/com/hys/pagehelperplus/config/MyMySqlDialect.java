package com.hys.pagehelperplus.config;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.helper.MySqlDialect;
import com.hys.pagehelperplus.util.PageHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;

import java.util.List;
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

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        if (log.isDebugEnabled()) {
            log.debug("原始SQL：\n" + sql);
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
        Matcher m = PATTERN.matcher(sql);
        if (m.find()) {
            //SELECT后面FROM前面的查找字段
            fields = m.group(1);
            for (String keyName : keyNames) {
                if (fields.contains(keyName)) {
                    fields = fields.replace(keyName, "alias1." + keyName);
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

        String returnSql = "SELECT " + fields + " " + fromTable + " alias1 \n" +
                " INNER JOIN ( SELECT " + getKeyNames(keyNames) + " " + afterClause + " ) alias2"
                + joinKeyNames(keyNames);
        if (log.isDebugEnabled()) {
            log.debug("拼接后的分页SQL：\n" + returnSql);
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
            stringBuilder.append("alias1.").append(keyNames.get(i)).append(" = alias2.").append(keyNames.get(i));
            if (i != keyNames.size() - 1) {
                stringBuilder.append(" AND ");
            }
        }
        return stringBuilder.toString();
    }
}