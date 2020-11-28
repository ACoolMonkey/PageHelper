package com.hys.pagehelperplus.util;

import com.github.pagehelper.Page;
import com.hys.pagehelperplus.entity.Pager;
import com.hys.pagehelperplus.util.mapstruct.PageHelperMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PageHelper工具类
 *
 * @author Robert Hou
 * @since 2020年11月27日 16:19
 **/
public class PageHelperUtils<T> {

    /**
     * 默认的表主键名为“id”
     */
    private static final ThreadLocal<List<String>> KEY_NAMES = ThreadLocal.withInitial(ArrayList::new);

    private PageHelperUtils() {
    }

    /**
     * PageHelper中的Page类转换成自定义的Pager类
     *
     * @see Page
     * @see Pager
     */
    public static <T> Pager<T> pageTransform(Page<T> page) {
        return PageHelperMapper.INSTANCE.pageTransform(
                page.getStartRow(),
                page.getEndRow(),
                page.getPageNum(),
                page.getPageSize(),
                page.getPages(),
                page.getTotal(),
                page.getResult());
    }

    public static List<String> getKeyNames() {
        return KEY_NAMES.get();
    }

    public static void setKeyNames(String[] keyNames) {
        List<String> list = new ArrayList<>(Arrays.asList(keyNames));
        KEY_NAMES.set(list);
    }

    /**
     * 清空ThreadLocal值，防止内存泄漏
     */
    public static void remove() {
        KEY_NAMES.remove();
    }
}
