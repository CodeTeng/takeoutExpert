package com.lt.reggie.controller;

import com.lt.reggie.common.Result;
import com.lt.reggie.entity.Orders;
import com.lt.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 订单管理器
 * @author: 狂小腾
 * @date: 2022/5/30 12:10
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {
        log.info("用户下单:{}", orders.toString());
        ordersService.submit(orders);
        return Result.success("下单成功");
    }
}
