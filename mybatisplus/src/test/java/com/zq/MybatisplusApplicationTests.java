package com.zq;

import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
// 增加运行器
@RunWith(SpringRunner.class)
class MybatisplusApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void selectAll() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
	}

	@Test
	public void insert() {
		User user = new User();
		user.setRealName("小红");
		user.setAge(31);
		user.setEmail("1@qq.com");
		user.setManagerId(1087982257332887553L);
		user.setCreateTime(LocalDateTime.now());

		/**
		 * 默认ID是用雪花算法生成的，数据库中是 manager_id
		 */
		int rows = userMapper.insert(user);
		System.out.println("受影响的行数：" + rows);
	}
}
