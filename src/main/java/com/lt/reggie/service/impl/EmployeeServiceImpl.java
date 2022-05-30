package com.lt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.reggie.entity.Employee;
import com.lt.reggie.service.EmployeeService;
import com.lt.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
 * @author teng
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2022-05-26 15:05:44
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

}




