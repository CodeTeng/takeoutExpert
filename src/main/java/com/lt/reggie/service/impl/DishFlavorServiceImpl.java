package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.entity.DishFlavor;
import com.lt.reggie.service.DishFlavorService;
import com.lt.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-05-26 15:05:44
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




