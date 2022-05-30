package com.lt.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.DishDto;
import com.lt.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author teng
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2022-05-26 15:05:44
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时保存对应的口味数据
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品和对应口味信息
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 分页查询
     */
    Result<Page> pageInfo(Integer page, Integer pageSize, String name);

    Result<List<DishDto>> getList(Dish dish);

    /**
     * 根据菜品id逻辑删除菜品和对应的口味
     */
    Result<String> delete(Long id);

    /**
     * 根据菜品id批量删除菜品和对应的口味
     */
    Result<String> deleteBatchByIds(Long[] id);
}
