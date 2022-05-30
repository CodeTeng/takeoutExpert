package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lt.reggie.common.BaseContext;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.entity.AddressBook;
import com.lt.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 地址薄管理
 * @author: 狂小腾
 * @date: 2022/5/30 12:09
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址薄
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址薄：{}", addressBook.toString());
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     */
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {
       log.info("修改地址：{}", addressBook.toString());
       addressBookService.updateById(addressBook);
       return Result.success("修改成功");
    }

    /**
     * 设置为默认地址
     */
    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置为默认地址：{}", addressBook.toString());
        // 先将该用户下的地址修改为不是默认地址
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);

        // 再更新唯一的默认地址
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        log.info("根据id查询地址：{}", id);
        if (id == null || id <= 0) {
            throw new CustomException("参数错误");
        }
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有查询到默认地址");
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        log.info("查询指定用户的全部地址：{}", addressBook.toString());
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return Result.success(list);
    }

}
