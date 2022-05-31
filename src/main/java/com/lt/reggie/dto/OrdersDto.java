package com.lt.reggie.dto;

import com.lt.reggie.entity.OrderDetail;
import com.lt.reggie.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OrdersDTO
 * @author teng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto extends Orders {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 订单明细集合
     */
    private List<OrderDetail> orderDetails;
}
