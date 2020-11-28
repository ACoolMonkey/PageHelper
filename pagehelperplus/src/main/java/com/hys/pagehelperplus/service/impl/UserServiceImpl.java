package com.hys.pagehelperplus.service.impl;

import com.github.pagehelper.Page;
import com.hys.pagehelperplus.entity.Pager;
import com.hys.pagehelperplus.entity.UserDO;
import com.hys.pagehelperplus.entity.UserVO;
import com.hys.pagehelperplus.manager.UserManager;
import com.hys.pagehelperplus.service.UserService;
import com.hys.pagehelperplus.util.PageHelperUtils;
import com.hys.pagehelperplus.util.mapstruct.UserListMapper;
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
        Page<UserDO> list = userManager.list(pageNum, pageSize);
        Pager<UserDO> pager = PageHelperUtils.pageTransform(list);
        return UserListMapper.INSTANCE.userDO2VOList(pager);
    }
}
