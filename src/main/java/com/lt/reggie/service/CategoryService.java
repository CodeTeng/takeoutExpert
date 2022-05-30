package com.lt.reggie.service;

import com.lt.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service
 * @createDate 2022-05-26 15:05:44
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id 分类id
     */
    void remove(Long id);
}
