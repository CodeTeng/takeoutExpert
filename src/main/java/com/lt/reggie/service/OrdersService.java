package com.lt.reggie.service;

import com.lt.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【orders(订单表)】的数据库操作Service
 * @createDate 2022-05-26 15:05:44
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     */
    void submit(Orders orders);
}
