package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.entity.OrderDetail;
import com.lt.reggie.service.OrderDetailService;
import com.lt.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-05-26 15:05:44
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




