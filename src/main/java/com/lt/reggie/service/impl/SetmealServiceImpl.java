package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.SetmealDto;
import com.lt.reggie.entity.Category;
import com.lt.reggie.entity.Setmeal;
import com.lt.reggie.entity.SetmealDish;
import com.lt.reggie.mapper.CategoryMapper;
import com.lt.reggie.mapper.SetmealDishMapper;
import com.lt.reggie.service.SetmealDishService;
import com.lt.reggie.service.SetmealService;
import com.lt.reggie.mapper.SetmealMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author teng
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2022-05-26 15:05:44
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        String name = setmealDto.getName();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(name), Setmeal::getName, name).last("limit 1");
        Setmeal setmeal = setmealMapper.selectOne(queryWrapper);
        if (setmeal != null) {
            // 已经存在
            throw new CustomException(name + "已存在，不能重复添加");
        }
        // 保存套餐的基本信息，操作setmeal，执行insert操作
        setmealMapper.insert(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        Long count = setmealMapper.selectCount(lambdaQueryWrapper);
        if (count > 0) {
            // 如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        // 可以删除，先删除套餐表中的数据---setmeal
        setmealMapper.deleteBatchIds(ids);
        // 再删除关系表中的数据----setmeal_dish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);
    }

    /**
     * 分页查询
     */
    @Override
    public Result<Page> pageInfo(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealMapper.selectPage(pageInfo, setmealLambdaQueryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(setmealDtoList);
        return Result.success(dtoPage);
    }

    /**
     * 根据id查询对应套餐和菜品信息
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal == null) {
            throw new CustomException("未查询到该套餐");
        }
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * 修改套餐和对应的菜品信息
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        // 修改基本Setmeal信息
        setmealMapper.updateById(setmealDto);

        // 先删除套餐对应的菜品信息 --- 会使数据库产生冗余 TODO 需要优化(逻辑删除产生)
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);

        // 再添加
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}




