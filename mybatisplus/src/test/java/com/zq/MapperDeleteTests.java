package com.zq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
// 增加运行器
@RunWith(SpringRunner.class)
public class MapperDeleteTests {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据 id 删除一条记录
     */
    @Test
    public void deleteById() {
        int rows = userMapper.deleteById(1493145434523213825L);

        System.out.println("受影响的行数：" + rows);
    }

    /**
     * 根据 id 批量删除
     */
    @Test
    public void deleteBatchIds() {
        List<Long> longs = Arrays.asList(1493148653144035330L, 1493145131434422274L);

        int rows = userMapper.deleteBatchIds(longs);

        System.out.println("受影响的行数：" + rows);
    }

    /**
     * 根据 map 来删除
     */
    @Test
    public void deleteByMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", "小黄1");
        map.put("age", "32");

        int rows = userMapper.deleteByMap(map);

        System.out.println("受影响的行数：" + rows);
    }

    /**
     * 以条件构造器为参数的删除方法
     */
    @Test
    public void deleteByWrapper() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();

        lambdaQueryWrapper.eq(User::getRealName, "小红1").eq(User::getAge, 34);

        int rows = userMapper.delete(lambdaQueryWrapper);

        System.out.println("受影响的行数：" + rows);
    }
}
