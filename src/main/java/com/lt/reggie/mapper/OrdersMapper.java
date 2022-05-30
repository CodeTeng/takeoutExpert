package com.lt.reggie.mapper;

import com.lt.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【orders(订单表)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.Orders
 */
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {

}




