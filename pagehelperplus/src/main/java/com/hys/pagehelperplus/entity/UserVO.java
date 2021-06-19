package com.hys.pagehelperplus.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户展示对象
 *
 * @author Robert Hou
 * @since 2020年11月27日 22:23
 **/
@Data
@NoArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = -8871248796230316578L;
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
