package com.hys.pagehelperplus.demo.service;

import com.hys.pagehelperplus.demo.entity.UserVO;
import com.hys.pagehelperplus.entity.Pager;

/**
 * 用户服务接口
 *
 * @author Robert Hou
 * @since 2020年11月27日 21:46
 **/
public interface UserService {

    /**
     * 分页查看用户信息
     *
     * @param pageNum  页号
     * @param pageSize 页大小
     * @return 用户信息
     */
    Pager<UserVO> list(int pageNum, int pageSize);
}
