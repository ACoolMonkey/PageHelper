package com.hys.pagehelperplus.util.mapstruct;

import com.hys.pagehelperplus.entity.Pager;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * PageHelper映射
 *
 * @author Robert Hou
 * @since 2020年11月27日 16:22
 **/
@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PageHelperMapper {

    PageHelperMapper INSTANCE = Mappers.getMapper(PageHelperMapper.class);

    /**
     * PageHelper中的Page类转换成自定义的Pager类
     *
     * @see Pager
     */
    Pager pageTransform(Long startRow,
                        Long endRow,
                        Integer pageNum,
                        Integer pageSize,
                        Integer pages,
                        Long total,
                        List list);
}
