package com.zq;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
// 增加运行器
@RunWith(SpringRunner.class)
public class MapperUpdateTests {

    @Autowired
    private UserMapper userMapper;

    /**
     * updateById() 一个参数实体对象，它会出现在 mysql update 语句的 set 语句中
     *
     * UPDATE mp_user SET name=?, age=? WHERE id=?
     */
    @Test
    public void updateById() {
        User user = new User();

        user.setId(1087982257332887553L);
        user.setRealName("大大大boss");
        user.setAge(9999);

        int rows = userMapper.updateById(user);
        System.out.println("受影响的记录数为：" + rows);
    }

    /**
     * update() 方法有两个参数
     *
     * 第一个参数是实体对象，它会再现在 update set 语句中
     * 第二个参数是条件构造器，它会出现在 update where 语句中
     *
     * 与 queryWrapper 一样，可以有多种方法构造
     *
     * LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
     *
     * UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
     *
     * UpdateWrapper<User> updateWrapper = Wrappers.<User>update();
     *
     *
     * UPDATE mp_user SET age=?, email=? WHERE (name = ? AND age = ?)
     */
    @Test
    public void updateByLambdaUpdateWrapper() {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();

        lambdaUpdateWrapper.eq(User::getRealName, "王天风").eq(User::getAge, 25);

        User user = new User();
        user.setAge(26);
        user.setEmail("111@qq.com");

        int rows = userMapper.update(user, lambdaUpdateWrapper);
        System.out.println("受影响的记录数为：" + rows);
    }

    /**
     * 使用 Lambda 语法更新0
     */
    @Test
    public void updateByLambdaUpdateWrapper2() {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();

        lambdaUpdateWrapper.eq(User::getRealName, "小张").set(User::getRealName, "小小张");

        int rows = userMapper.update(null, lambdaUpdateWrapper);

        System.out.println("受影响的记录数为：" + rows);
    }

    /**
     * 直接把实体对象传进去作为 where 条件
     *
     * UPDATE mp_user SET age=? WHERE name<>? AND (age = ?)
     */
    @Test
    public void updateByUpdateWrapper() {
        User whereUser = new User();
        whereUser.setRealName("李艺伟1");

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);
        updateWrapper.eq("age", 28);

        User setUser = new User();
        setUser.setAge(90);

        int rows = userMapper.update(setUser, updateWrapper);
        System.out.println("受影响的记录数为：" + rows);

    }

    /**
     * 条件构造器中 set 方法的使用
     *
     * 不创建实体传入，直接在条件中使用 set 方法
     */
    @Test
    public void updateByUpdateWrapper2() {
        UpdateWrapper<User> updateWrapper = Wrappers.<User>update();

        updateWrapper.eq("name", "刘红雨").set("age", 33).set("email", "");

        int rows = userMapper.update(null, updateWrapper);
        System.out.println("受影响的记录数为：" + rows);
    }



    /**
     * 使用 Lambda 链式调用来更新
     */
    @Test
    public void updateByLambdaUpdateChainWrapper() {
        boolean update = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getRealName, "小红1")
                .eq(User::getAge, 132)
                .set(User::getEmail, "小红1@qq.com")
                .update();

        System.out.println("更新成功：" + update);
    }
}
