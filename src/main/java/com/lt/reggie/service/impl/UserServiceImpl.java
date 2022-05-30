package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.entity.User;
import com.lt.reggie.service.UserService;
import com.lt.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-05-26 15:05:44
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




