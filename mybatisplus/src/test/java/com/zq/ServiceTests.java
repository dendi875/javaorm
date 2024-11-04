package com.zq;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zq.entity.User;
import com.zq.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
// 增加运行器
@RunWith(SpringRunner.class)
public class ServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void getOne() {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 30), false);
        System.out.println(user);
    }

    @Test
    public void listByIds() {
        List<Long> ids = Arrays.asList(1087982257332887553L, 1088248166370832385L);
        List<User> users = userService.listByIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    public void saveBatch() {
        User use1 = new User();
        use1.setRealName("小白");
        use1.setAge(30);

        User use2 = new User();
        use2.setRealName("大白");
        use2.setAge(31);

        List<User> users = Arrays.asList(use1, use2);

        boolean saveBatch = userService.saveBatch(users);
        System.out.println("是否插入成功：" + saveBatch);
    }

    @Test
    public void saveOrUpdate() {
        User user = new User();
        user.setId(1495330236349317121L);
        user.setRealName("大黄11111");
        user.setAge(32);

        boolean saveOrUpdate = userService.saveOrUpdate(user);
        System.out.println("是否成功：" + saveOrUpdate);
    }

    @Test
    public void remove() {
        boolean remove = userService.removeById(1495338806293889025L);
        System.out.println("是否删除成功：" + remove);
    }

    @Test
    public void chainQuery() {
        List<User> users = userService.lambdaQuery().gt(User::getAge, 30).like(User::getRealName, "小").list();

        users.forEach(System.out::println);
    }

    @Test
    public void chainUpdate() {
        boolean update = userService.lambdaUpdate().eq(User::getAge, 9999).set(User::getAge, 100).update();

        System.out.println("是否成功：" + update);
    }

    @Test
    public void chainRemove() {
        boolean remove = userService.lambdaUpdate().eq(User::getRealName, "大白").remove();

        System.out.println("是否成功：" + remove);
    }


}
