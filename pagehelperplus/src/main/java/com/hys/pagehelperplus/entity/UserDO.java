package com.hys.pagehelperplus.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户表对象
 *
 * @author Robert Hou
 * @since 2020年11月27日 22:25
 **/
@Data
@NoArgsConstructor
public class UserDO implements Serializable {

    private static final long serialVersionUID = 7368535729213662088L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private Boolean sex;
    /**
     * 手机
     */
    private String phone;
    /**
     * 地址
     */
    private String address;
}
