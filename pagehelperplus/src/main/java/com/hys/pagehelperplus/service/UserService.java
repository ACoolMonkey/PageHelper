package com.hys.pagehelperplus.service;

import com.hys.pagehelperplus.entity.Pager;
import com.hys.pagehelperplus.entity.UserVO;

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
     * @param pageNum
     * @param pageSize
     * @return 用户信息
     */
    Pager<UserVO> list(int pageNum, int pageSize);
}
