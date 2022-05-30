package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.entity.Category;
import com.lt.reggie.entity.Dish;
import com.lt.reggie.entity.Setmeal;
import com.lt.reggie.mapper.DishMapper;
import com.lt.reggie.mapper.SetmealMapper;
import com.lt.reggie.service.CategoryService;
import com.lt.reggie.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author teng
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2022-05-26 15:05:44
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id 分类id
     */
    @Override
    public void remove(Long id) {
        // 添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        Long dishCount = dishMapper.selectCount(dishLambdaQueryWrapper);
        // 查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (dishCount > 0) {
            // 已经关联了菜品，需要抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件，根据分类id进行查询
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        Long setMealCount = setmealMapper.selectCount(setMealLambdaQueryWrapper);
        if (setMealCount > 0) {
            // 已经关联了套餐，需要抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        // 正常删除分类
        categoryMapper.deleteById(id);
    }
}




