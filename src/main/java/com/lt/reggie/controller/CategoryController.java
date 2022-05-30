package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.PageDto;
import com.lt.reggie.entity.Category;
import com.lt.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 分类管理
 * @author: 狂小腾
 * @date: 2022/5/27 16:18
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        log.info("分类：{}", category.toString());
        String name = category.getName();
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Category::getName, name);
        Category result = categoryService.getOne(queryWrapper);
        if (result != null) {
            throw new CustomException(name + "已经存在，不能重复添加");
        }
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(PageDto pageDto) {
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        if (page == null || pageSize == null || page <= 0 || pageSize <= 0) {
            throw new CustomException("分页参数错误");
        }
        log.info("page:{}，pageSize:{}", page, pageSize);
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据id删除分类
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        if (id == null || id <= 0) {
            throw new CustomException("参数错误");
        }
        log.info("删除分类：{}", id);
        categoryService.remove(id);
        return Result.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category.toString());
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }
}
