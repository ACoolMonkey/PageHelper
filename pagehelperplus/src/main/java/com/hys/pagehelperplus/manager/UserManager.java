package com.hys.pagehelperplus.manager;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hys.pagehelperplus.annotation.KeyNamesStrategy;
import com.hys.pagehelperplus.constant.KeyNamesStrategyEnum;
import com.hys.pagehelperplus.dao.UserDAO;
import com.hys.pagehelperplus.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户Manager
 *
 * @author Robert Hou
 * @since 2020年11月28日 10:59
 **/
@Component
public class UserManager {

    @Autowired
    private UserDAO userDAO;

    @KeyNamesStrategy(strategy = KeyNamesStrategyEnum.MUST_TELL, keyNames = {"id", "name"})
    public Page<UserDO> list(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userDAO.list();
    }
}
