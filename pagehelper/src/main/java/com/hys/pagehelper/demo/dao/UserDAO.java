package com.hys.pagehelper.demo.dao;

import com.github.pagehelper.Page;
import com.hys.pagehelper.demo.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author Robert Hou
 * @since 2020年11月27日 22:37
 **/
@Mapper
public interface UserDAO {

    Page<UserDO> list();
}
