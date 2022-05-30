package com.lt.reggie.mapper;

import com.lt.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【dish(菜品管理)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.Dish
 */
@Repository
public interface DishMapper extends BaseMapper<Dish> {

}




