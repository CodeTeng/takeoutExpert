package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.DishDto;
import com.lt.reggie.dto.PageDto;
import com.lt.reggie.dto.SetmealDto;
import com.lt.reggie.entity.Category;
import com.lt.reggie.entity.Dish;
import com.lt.reggie.entity.Setmeal;
import com.lt.reggie.mapper.CategoryMapper;
import com.lt.reggie.mapper.SetmealDishMapper;
import com.lt.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 套餐管理
 * @author: 狂小腾
 * @date: 2022/5/28 18:54
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增套餐
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return Result.success("新增套餐成功");
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(PageDto pageDto) {
        if (pageDto == null) {
            throw new CustomException("分页参数错误");
        }
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        String name = pageDto.getName();
        if (page == null || pageSize == null || page <= 0 || pageSize <= 0) {
            throw new CustomException("分页参数错误");
        }
        log.info("page:{},pageSize:{},name:{}", page, pageSize, name);
        return setmealService.pageInfo(page, pageSize, name);
    }

    /**
     * 根据id查询对应套餐和菜品信息
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> get(@PathVariable Long id) {
        log.info("根据id查询对应的套餐和菜品信息：{}", id);
        if (id == null || id < 0) {
            throw new CustomException("参数错误");
        }
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return Result.success(setmealDto);
    }

    /**
     * 修改套餐和对应的菜品信息
     */
    @PutMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改套餐：{}", setmealDto.toString());
        setmealService.updateWithDish(setmealDto);
        return Result.success("修改套餐信息成功");
    }

    /**
     * (批量)更改菜品售卖状态
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> sellout(Long[] ids, @PathVariable Integer status) {
        if (ids == null || ids.length <= 0) {
            throw new CustomException("参数错误");
        }
        if (ids.length <= 1) {
            // 单个更改售卖状态
            Setmeal setmeal = setmealService.getById(ids[0]);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        } else {
            // 批量更改
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Setmeal::getId, ids);
            List<Setmeal> setmealList = setmealService.list(queryWrapper);
            setmealList.stream().map(item -> {
                item.setStatus(status);
                return item;
            }).collect(Collectors.toList());
            setmealService.updateBatchById(setmealList);
        }
        return Result.success("更改状态成功");
    }

    /**
     * 删除套餐
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐:{}", ids);
        setmealService.removeWithDish(ids);
        return Result.success("套餐数据删除成功");
    }

    /**
     * 查询套餐信息
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public Result<List<Setmeal>> list(Setmeal setmeal) {
        log.info("查询套餐信息：{}", setmeal.toString());
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(setmeal.getName()), Setmeal::getName, setmeal.getName());
        lambdaQueryWrapper.eq(null != setmeal.getCategoryId(), Setmeal::getCategoryId, setmeal.getCategoryId());
        lambdaQueryWrapper.eq(null != setmeal.getStatus(), Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return Result.success(list);
    }
}
