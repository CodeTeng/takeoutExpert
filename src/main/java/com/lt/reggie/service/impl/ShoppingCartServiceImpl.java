package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.common.BaseContext;
import com.lt.reggie.common.Result;
import com.lt.reggie.entity.ShoppingCart;
import com.lt.reggie.service.ShoppingCartService;
import com.lt.reggie.mapper.ShoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author teng
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2022-05-26 15:05:44
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加购物车
     */
    @Override
    public Result<ShoppingCart> add(ShoppingCart shoppingCart) {
        ShoppingCart result = getShoppingCart(shoppingCart);
        if (result != null) {
            // 如果已经存在，就在原来数量基础上加一
            Integer number = result.getNumber();
            result.setNumber(number + 1);
            shoppingCartMapper.updateById(result);
        } else {
            // 不存在就添加到购物车中，数量默认是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            result = shoppingCart;
        }
        return Result.success(result);
    }

    /**
     * 减少购物查菜品或套餐数量
     */
    @Override
    public Result<ShoppingCart> sub(ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            // 减少菜品数量
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            // 减少的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart result = shoppingCartMapper.selectOne(queryWrapper);
        Integer number = result.getNumber();
        if (number == 1) {
            // 清空购物车
            shoppingCartMapper.delete(queryWrapper);
        } else {
            // 数量减少1
            result.setNumber(number - 1);
            shoppingCartMapper.updateById(result);
        }
        return Result.success(result);
    }

    /**
     * 公共方法 待使用
     */
    private ShoppingCart getShoppingCart(ShoppingCart shoppingCart) {
        // 设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            // 添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            // 添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        // 查询当前菜品或者套餐是否在购物车中
        ShoppingCart result = shoppingCartMapper.selectOne(queryWrapper);
        return result;
    }
}




