package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.entity.AddressBook;
import com.lt.reggie.service.AddressBookService;
import com.lt.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-05-26 15:05:44
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




