package com.zq;

import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest
// 增加运行器
@RunWith(SpringRunner.class)
public class MapperInsertTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setRealName("小黄11111");
        user.setAge(32);
        user.setEmail("32@qq.com");
        user.setManagerId(1087982257332887553L);
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("我是备注数据库中没有这个字段");

        /**
         * 默认ID是用雪花算法生成的，数据库中是 manager_id
         */
        int rows = userMapper.insert(user);
        System.out.println("受影响的行数：" + rows);
    }
}
