package com.lt.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.SetmealDto;
import com.lt.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author teng
 * @description 针对表【setmeal(套餐)】的数据库操作Service
 * @createDate 2022-05-26 15:05:44
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     */
    void removeWithDish(List<Long> ids);

    /**
     * 分页查询
     */
    Result<Page> pageInfo(Integer page, Integer pageSize, String name);

    /**
     * 根据id查询对应套餐和菜品信息
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 修改套餐和对应的菜品信息
     */
    void updateWithDish(SetmealDto setmealDto);
}
