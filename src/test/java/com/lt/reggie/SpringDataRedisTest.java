package com.lt.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: Redis的测试类
 * @author: 狂小腾
 * @date: 2022/5/30 21:17
 */
@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String数据类型
     */
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city123", "beijing");
        String value = (String) redisTemplate.opsForValue().get("city123");
        System.out.println(value);
        redisTemplate.opsForValue().set("key1", "value1", 10L, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
        System.out.println(aBoolean);
    }

    /**
     * 操作Hash数据类型
     */
    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        // 存值
        hashOperations.put("002", "name", "xiaoming");
        hashOperations.put("002", "age", "20");
        hashOperations.put("002", "address", "bj");

        // 取值
        String name = (String) hashOperations.get("002", "name");
        System.out.println(name);

        // 获得hash结构中的所有字段
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        // 获取hash结构中的所有value
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作List数据类型
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        // 存值
        listOperations.leftPush("mylist", "a");
        listOperations.leftPushAll("mylist", "b", "c", "d");

        // 取值
        List mylist = listOperations.range("mylist", 0, -1);
        for (Object o : mylist) {
            System.out.println(o);
        }

        // 获取列表长度
        Long size = listOperations.size("mylist");
        for (int i = 0; i < size; i++) {
            // 出队列
            String element = (String) listOperations.rightPop("mylist");
            System.out.println(element);
        }
    }

    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        // 存值
        setOperations.add("myset", "a", "b", "c", "d");

        // 取值
        Set myset = setOperations.members("myset");
        for (Object o : myset) {
            System.out.println(o);
        }

        // 删除成员
        setOperations.remove("myset", "a", "b");

        // 取值
        myset = setOperations.members("myset");
        for (Object o : myset) {
            System.out.println(o);
        }
    }

    /**
     * 操作Zset数据类型
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        // 存值
        zSetOperations.add("myZset", "a", 10.0);
        zSetOperations.add("myZset", "b", 11.0);
        zSetOperations.add("myZset", "c", 12.0);
        zSetOperations.add("myZset", "a", 13.0);

        // 取值
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String o : myZset) {
            System.out.println(o);
        }

        // 修改分数
        zSetOperations.incrementScore("myZset", "b", 20.0);

        // 取值
        myZset = zSetOperations.range("myZset", 0, -1);
        for (String o : myZset) {
            System.out.println(o);
        }

        // 删除成员
        zSetOperations.remove("myZset", "a", "b");

        // 取值
        myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }
    }

    /**
     * 通用操作，针对不同的数据类型都可以操作
     */
    @Test
    public void testCommon() {
        // 取出redis中所有的keys
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        // 判断某个key是否存在
        Boolean flag = redisTemplate.hasKey("kxt");
        System.out.println(flag);

        // 删除指定key
        redisTemplate.delete("myZset");

        // 获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType.name());
    }
}
