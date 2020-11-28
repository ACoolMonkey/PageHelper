package com.hys.pagehelperplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hys.pagehelperplus.dao")
public class PagehelperplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagehelperplusApplication.class, args);
    }

}
