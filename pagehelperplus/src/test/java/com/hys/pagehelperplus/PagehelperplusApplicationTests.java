package com.hys.pagehelperplus;

import com.hys.pagehelperplus.demo.entity.UserVO;
import com.hys.pagehelperplus.demo.service.UserService;
import com.hys.pagehelperplus.entity.Pager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PagehelperplusApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        Pager<UserVO> list = userService.list(1, 10);
        System.out.println(list);
    }
}
