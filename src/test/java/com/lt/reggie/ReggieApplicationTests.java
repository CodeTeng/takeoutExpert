package com.lt.reggie;

import com.lt.reggie.entity.Employee;
import com.lt.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        List<Employee> list = employeeService.list();
        list.forEach(System.out::println);
    }

}
