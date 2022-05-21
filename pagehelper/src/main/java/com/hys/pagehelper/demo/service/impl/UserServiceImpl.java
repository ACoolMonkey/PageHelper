package com.hys.pagehelper.demo.service.impl;

import com.hys.pagehelper.demo.entity.UserDO;
import com.hys.pagehelper.demo.entity.UserVO;
import com.hys.pagehelper.demo.manager.UserManager;
import com.hys.pagehelper.demo.service.UserService;
import com.hys.pagehelper.demo.util.mapstruct.UserListMapper;
import com.hys.pagehelper.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口实现
 *
 * @author Robert Hou
 * @since 2020年11月27日 21:47
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Pager<UserVO> list(int pageNum, int pageSize) {
        Pager<UserDO> pager = userManager.list(pageNum, pageSize);
        return UserListMapper.INSTANCE.userDO2VOList(pager);
    }
}
