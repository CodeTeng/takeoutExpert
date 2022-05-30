package com.lt.reggie.dto;

import com.lt.reggie.entity.Setmeal;
import com.lt.reggie.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 套餐DTO
 * @author: 狂小腾
 * @date: 2022/5/29 12:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDto extends Setmeal {
    /**
     * 套餐菜品关系集合
     */
    private List<SetmealDish> setmealDishes;

    /**
     * 套餐分类名称
     */
    private String categoryName;
}
