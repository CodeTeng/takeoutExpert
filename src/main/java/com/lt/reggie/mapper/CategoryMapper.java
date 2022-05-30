package com.lt.reggie.mapper;

import com.lt.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.Category
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

}




