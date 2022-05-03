package com.hys.pagehelperplus.util;

import com.github.pagehelper.Page;
import com.hys.pagehelperplus.entity.Pager;
import com.hys.pagehelperplus.util.mapstruct.PageHelperMapper;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PageHelper工具类
 *
 * @author Robert Hou
 * @since 2020年11月27日 16:19
 **/
public class PageHelperUtils {

    /**
     * 默认的表主键名为“id”
     */
    private static final ThreadLocal<List<String>> KEY_NAMES = ThreadLocal.withInitial(ArrayList::new);
    /**
     * 是否降级
     */
    private static final ThreadLocal<Boolean> IS_RELEGATED = new ThreadLocal<>();

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

    /**
     * PageHelper中的Page类转换成自定义的Pager类
     *
     * @see Page
     * @see Pager
     */
    public static <T> Pager<T> pageTransform(Page<?> page, List<T> list) {
        return PageHelperMapper.INSTANCE.pageTransform(
                page.getStartRow(),
                page.getEndRow(),
                page.getPageNum(),
                page.getPageSize(),
                page.getPages(),
                page.getTotal(),
                list);
    }

    public static List<String> getKeyNames() {
        return KEY_NAMES.get();
    }

    public static void setKeyNames(String[] keyNames) {
        List<String> list = new ArrayList<>(Arrays.asList(keyNames));
        KEY_NAMES.set(list);
    }

    public static Boolean getIsRelegated() {
        Boolean isRelegated = IS_RELEGATED.get();
        if (isRelegated == null) {
            setIsRelegated(false);
            return false;
        }
        return isRelegated;
    }

    public static void setIsRelegated(boolean isRelegated) {
        IS_RELEGATED.set(isRelegated);
    }

    /**
     * 清空ThreadLocal值，防止内存泄漏
     */
    public static void remove() {
        KEY_NAMES.remove();
        IS_RELEGATED.remove();
    }

    /**
     * 内存分页
     *
     * @param list     缓存
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     * @return 分页数据
     */
    public static <T> Pager<T> cachePage(@NonNull List<T> list, int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException("页码或每页显示数量不能小于1！");
        }
        if (list.size() == 0) {
            return new Pager<>();
        }

        int totalRow = list.size();
        int totalPage;
        int start;
        int end;
        int power = is2Power(pageSize);
        if (power != -1) {
            totalPage = (totalRow + pageSize - 1) >>> power;
            start = (pageNum - 1) << power;
            end = Math.min(pageNum << power, totalRow);
        } else {
            totalPage = (totalRow + pageSize - 1) / pageSize;
            start = (pageNum - 1) * pageSize;
            end = Math.min(pageNum * pageSize, totalRow);
        }
        List<T> returnList = list.subList(start, end);

        return PageHelperMapper.INSTANCE.pageTransform(
                (long) start,
                (long) end,
                pageNum,
                pageSize,
                totalPage,
                (long) totalRow,
                returnList);
    }

    /**
     * 判断一个数是否是2的幂
     *
     * @param value 待判断的数
     * @return -1：不是2的幂；其他数：2的几次幂
     */
    private static int is2Power(int value) {
        if ((value & (value - 1)) != 0) {
            return -1;
        }
        return Integer.numberOfTrailingZeros(value);
    }
}
