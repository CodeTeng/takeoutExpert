package com.lt.reggie.mapper;

import com.lt.reggie.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.OrderDetail
 */
@Repository
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




