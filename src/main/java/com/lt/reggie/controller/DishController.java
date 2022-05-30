package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.DishDto;
import com.lt.reggie.dto.PageDto;
import com.lt.reggie.entity.Category;
import com.lt.reggie.entity.Dish;
import com.lt.reggie.entity.DishFlavor;
import com.lt.reggie.service.CategoryService;
import com.lt.reggie.service.DishFlavorService;
import com.lt.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @description: 菜品管理
 * @author: 狂小腾
 * @date: 2022/5/28 16:28
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        String name = dishDto.getName();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Dish::getName, name);
        Dish dish = dishService.getOne(queryWrapper);
        if (dish != null) {
            throw new CustomException(name + "已存在，不能重复添加");
        }
        log.info("新增菜品：{}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
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
        return dishService.pageInfo(page, pageSize, name);

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     */
    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id) {
        log.info("根据id查询对应菜品和口味信息：{}", id);
        if (id == null || id < 0) {
            throw new CustomException("参数错误");
        }
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品和对应口味信息
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品：{}", dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改菜品信息成功");
    }

    /**
     * 根据分类id获取菜品以及口味信息
     */
    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish) {
        log.info("菜品信息：{}", dish.toString());
        Long categoryId = dish.getCategoryId();
        if (categoryId == null || categoryId < 0) {
            throw new CustomException("参数错误");
        }
        return dishService.getList(dish);
    }

    /**
     * (批量)更改菜品售卖状态
     */
    @PostMapping("/status/{status}")
    public Result<String> sellout(Long[] ids, @PathVariable Integer status) {
        if (ids == null || ids.length <= 0) {
            throw new CustomException("参数错误");
        }
        if (ids.length <= 1) {
            // 单个更改售卖状态
            Dish dish = dishService.getById(ids[0]);
            dish.setStatus(status);
            dishService.updateById(dish);
        } else {
            // 批量更改
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Dish::getId, ids);
            List<Dish> dishList = dishService.list(queryWrapper);
            dishList.stream().map(item -> {
                item.setStatus(status);
                return item;
            }).collect(Collectors.toList());
            dishService.updateBatchById(dishList);
        }
        return Result.success("更改状态成功");
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    public Result<String> delete(Long[] id) {
        if (id == null || id.length <= 0) {
            throw new CustomException("参数错误");
        }
        if (id.length == 1) {
            // 单个售出
            Dish dish = dishService.getById(id[0]);
            Integer status = dish.getStatus();
            String name = dish.getName();
            if (status == 1) {
                throw new CustomException(name + "处于售出状态，无法删除");
            }
            return dishService.delete(id[0]);
        } else {
               LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
               queryWrapper.in(Dish::getId, id);
            List<Dish> dishList = dishService.list(queryWrapper);
            dishList.stream().forEach(item -> {
                if (item.getStatus() == 1) {
                    String name = item.getName();
                    throw new CustomException(name + "处于售出状态，无法删除");
                }
            });
            return dishService.deleteBatchByIds(id);
        }
    }
}
