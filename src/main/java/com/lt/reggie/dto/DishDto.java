package com.lt.reggie.dto;

import com.lt.reggie.entity.Dish;
import com.lt.reggie.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 菜品DTO
 * @author: 狂小腾
 * @date: 2022/5/28 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto extends Dish implements Serializable {

    /**
     * 菜品口味集合
     */
    private List<DishFlavor> flavors = new ArrayList<>();

    /**
     * 菜品分类名称
     */
    private String categoryName;

    /**
     * 份数
     */
    private Integer copies;
}
