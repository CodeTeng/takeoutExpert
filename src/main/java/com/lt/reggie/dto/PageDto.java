package com.lt.reggie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 分页DTO
 * @author: 狂小腾
 * @date: 2022/5/29 16:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    /**
     * 当前页
     */
    private Integer page;

    /**
     * 当前页显示多少页
     */
    private Integer pageSize;

    /**
     * 分页查询条件
     */
    private String name;
}
