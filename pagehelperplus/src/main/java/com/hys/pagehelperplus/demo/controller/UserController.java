package com.hys.pagehelperplus.demo.controller;

import com.hys.pagehelperplus.demo.entity.ResponseData;
import com.hys.pagehelperplus.demo.entity.UserVO;
import com.hys.pagehelperplus.demo.service.UserService;
import com.hys.pagehelperplus.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 *
 * @author Robert Hou
 * @since 2020年11月28日 11:57
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public ResponseData list(int pageNum, int pageSize) {
        Pager<UserVO> list = userService.list(pageNum, pageSize);
        return ResponseData.success(list);
    }
}
