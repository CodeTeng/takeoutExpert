package com.lt.reggie.service;

import com.lt.reggie.common.Result;
import com.lt.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service
 * @createDate 2022-05-26 15:05:44
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加购物车
     */
    Result<ShoppingCart> add(ShoppingCart shoppingCart);

    /**
     * 减少购物查菜品或套餐数量
     */
    Result<ShoppingCart> sub(ShoppingCart shoppingCart);
}
