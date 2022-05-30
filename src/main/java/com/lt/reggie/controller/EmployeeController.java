package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.reggie.common.CustomException;
import com.lt.reggie.common.Result;
import com.lt.reggie.dto.PageDto;
import com.lt.reggie.entity.Employee;
import com.lt.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: 狂小腾
 * @date: 2022/5/26 15:16
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        // 1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        // 3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return Result.error("登录失败");
        }
        // 4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误，请重新登录");
        }
        // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return Result.error("账号已禁用");
        }
        // 6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /**
     * 员工退出
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee) {
        log.info("新增员工：{}", employee.toString());
        // 根据身份证号进行判断是否重复添加
        String idNumber = employee.getIdNumber();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(idNumber), Employee::getIdNumber, idNumber);
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp != null) {
            // 已存在该员工，不能重复添加
            throw new CustomException("不能重复添加员工");
        }
        // 设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        // 以下用公共字段填充的方式进行实现 --> MP方式处理
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(PageDto pageDto) {
        if (pageDto == null) {
            throw new CustomException("分页参数错误");
        }
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        String name = pageDto.getName();
        if (page == null || pageSize == null || page <= 0 || pageSize <= 0) {
            throw new CustomException("分页参数错误");
        }
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        employeeService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     */
    @PutMapping
    public Result<String> update(@RequestBody Employee employee) {
        log.info("根据id修改员工信息：{}", employee.toString());

        // 公告字段填充的方式实现 --> MP实现
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        // employee.setUpdateTime(LocalDateTime.now());
        /*long id = Thread.currentThread().getId();
        log.info("线程id为：{}", id);*/

        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据id修改员工信息：{}", id);
        if (id == null || id <= 0) {
            throw new CustomException("参数错误");
        }
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("没有查询到对应的员工信息");
    }
}
