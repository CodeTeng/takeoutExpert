package com.lt.reggie.mapper;

import com.lt.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author teng
 * @description 针对表【address_book(地址管理)】的数据库操作Mapper
 * @createDate 2022-05-26 15:05:44
 * @Entity com.lt.reggie.entity.AddressBook
 */
@Repository
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




