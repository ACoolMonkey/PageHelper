package com.hys.pagehelper;

import com.hys.pagehelper.demo.entity.UserVO;
import com.hys.pagehelper.demo.service.UserService;
import com.hys.pagehelper.entity.Pager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PagehelperApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        Pager<UserVO> list = userService.list(1, 10);
        System.out.println(list);
    }
}
