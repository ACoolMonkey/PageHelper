package com.hys.pagehelper.demo.util.mapstruct;

import com.hys.pagehelper.demo.entity.UserDO;
import com.hys.pagehelper.demo.entity.UserVO;
import com.hys.pagehelper.entity.Pager;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * 用户映射
 *
 * @author Robert Hou
 * @since 2020年11月28日 11:05
 **/
@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserListMapper {

    UserListMapper INSTANCE = Mappers.getMapper(UserListMapper.class);

    /**
     * UserDO -> UserVO
     *
     * @see UserDO
     * @see UserVO
     */
    UserVO userDO2VO(UserDO userDO);

    /**
     * UserDO -> UserVO
     *
     * @see UserDO
     * @see UserVO
     */
    Pager<UserVO> userDO2VOList(Pager<UserDO> userDOList);
}
