package com.lt.reggie.mapper;

import com.lt.reggie.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【user(用户信息)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.User
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}




