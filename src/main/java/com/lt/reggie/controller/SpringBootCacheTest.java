package com.lt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lt.reggie.entity.User;
import com.lt.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: SpringBootCache测试类
 * @author: 狂小腾
 * @date: 2022/5/30 21:57
 */
@RestController
@RequestMapping("/cache")
@Slf4j
public class SpringBootCacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;

    /**
     * CachePut：将方法返回值放入缓存 --- 服务重启之后 缓存会失效
     * value：缓存的名称，每个缓存名称下面可以有多个key
     * key：缓存的key
     */
    @CachePut(value = "userCache", key = "#user.id")
    @PostMapping
    public User save(User user) {
        userService.save(user);
        return user;
    }

    /**
     * CacheEvict：清理指定缓存
     * value：缓存的名称，每个缓存名称下面可以有多个key
     * key：缓存的key
     */
    @CacheEvict(value = "userCache", key = "#id")
    // @CacheEvict(value = "userCache", key = "#p0")
    // @CacheEvict(value = "userCache", key = "#root.args[0]")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.removeById(id);
    }

    @CacheEvict(value = "userCache", key = "#user.id")
    // @CacheEvict(value = "userCache",key = "#p0.id")
    // @CacheEvict(value = "userCache",key = "#root.args[0].id")
    // @CacheEvict(value = "userCache",key = "#result.id")
    @PutMapping
    public User update(User user) {
        userService.updateById(user);
        return user;
    }

    /**
     * Cacheable：在方法执行前spring先查看缓存中是否有数据，如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中
     * value：缓存的名称，每个缓存名称下面可以有多个key
     * key：缓存的key
     * condition：条件，满足条件时才缓存数据
     * unless：满足条件则不缓存
     */
    @Cacheable(value = "userCache", key = "#id", unless = "result == null")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return user;
    }

    @Cacheable(value = "userCache", key = "#user.id + '_' + #user.phone")
    @GetMapping("/list")
    public List<User> list(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(user.getId() != null, User::getId, user.getId());
        queryWrapper.eq(user.getPhone() != null, User::getPhone, user.getPhone());
        List<User> list = userService.list(queryWrapper);
        return list;
    }
}
