package com.hys.pagehelper.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义分页对象
 *
 * @author Robert Hou
 * @since 2020年11月27日 22:15
 **/
@Data
@NoArgsConstructor
public class Pager<T> implements Serializable {

    private static final long serialVersionUID = -6243436389947501984L;

    /**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 起始行
     */
    private long startRow;
    /**
     * 末行
     */
    private long endRow;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 分页数据
     */
    private List<T> list;
}
