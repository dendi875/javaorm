package com.zq;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import org.assertj.core.internal.LongArrays;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		user.setRealName("小红1");
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

	/**
	 * 根据 ID 查询
	 */
	@Test
	public void selectById() {
		User user = userMapper.selectById(1087982257332887553L);
		System.out.println(user);
	}

	/**
	 * 查询（根据ID 批量查询）
	 */
	@Test
	public void selectBatchIds() {
		List<Long> idList = Arrays.asList(1087982257332887553L, 1088248166370832385L, 1088250446457389058L);
		List<User> users = userMapper.selectBatchIds(idList);
		users.forEach(System.out::println);
	}

	/**
	 * 查询（根据 columnMap 条件）
	 */
	@Test
	public void selectByMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("age", 31);	// 这里是数据库中的列名
		map.put("name", "小王");

		List<User> users = userMapper.selectByMap(map);
		users.forEach(System.out::println);
	}

	/*
	 * 1、名字中包含雨并且年龄小于40
	 * name like '%雨%' and age<40
	 * 条件构造器，两种方法
	 * 方法一. QueryWrapper<T> 泛型实参是实体类
	 * 方法二. 使用 Wrappers 的这个工具类，调用 query 方法
	 */
	@Test
	public void selectByWrapper() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		//QueryWrapper<User> queryWrapper1 = Wrappers.<User>query();	// 在调用的方法名字前面加入 <实参> 来显式指定泛型参数的类型
		queryWrapper.like("name", "红").lt("age", 40); // 这里的 name、age 是数据库中的列名，不是实体类的属性名

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 *  名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
	 *  name like '%雨%' and age between 20 and 40 and email is not null
	 */
	@Test
	public void setUserMapper2() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.like("name", "小").between("age", 30, 40).isNotNull("email");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
	 *  name like '王%' or age>=25 order by age desc,id asc
	 */
	@Test
	public void setUserMapper3() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.likeRight("name", "小").or().ge("age", 35).orderByDesc("age").orderByAsc("id");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 创建日期为2019年2月14日并且直属上级为名字为王姓
	 * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
	 */
	@Test
	public void setUserMapper4() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}","2022-01-22")
				.inSql("manager_id", "select id from mp_user where name like '%红%'");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/*
	 * 名字为王姓并且（年龄小于40或邮箱不为空）
	 * name like '王%' and (age<40 or email is not null)
	 */
	@Test
	public void setUserMapper5() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.likeRight("name", "小").and(qw -> qw.lt("age", 40).or().isNotNull("email"));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/*
	 * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
	 * name like '王%' or (age<40 and age>20 and email is not null)
	 */
	@Test
	public void setUserMapper6() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.likeRight("name", "小")
				.or(qw -> qw.lt("age", 40).gt("age", 30).isNotNull("email"));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * （年龄小于40或邮箱不为空）并且名字为王姓, nested 正常嵌套 不带 AND 或者 OR
	 * (age<40 or email is not null) and name like '王%'
	 */
	@Test
	public void setUserMapper7() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.nested(qw -> qw.lt("age", 30).or().isNotNull("email"))
				.likeRight("name", "王");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}


	/*
	 * 年龄为30、31、34、35
	 *   age in (30、31、34、35)
	 */
	@Test
	public void setUserMapper8() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.in("age", Arrays.asList(new Integer[]{20, 30, 31}));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 只返回一条语句
	 *
	 * limit 1
	 */
	@Test
	public void setUserMapper9() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.in("id", Arrays.asList(1, 2, 3)).last("limit 1");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 10、名字中包含雨并且年龄小于40(需求1加强版)
				第一种情况：  select id,name -- 就用select("")
							from user
							where name like '%雨%' and age<40
				第二种情况：  select id,name,age,email
							from user
							where name like '%雨%' and age<40
	 */
	@Test
	public void setUserMapper10() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.select("id", "name").in("age", Arrays.asList(30, 32, 33));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 10、名字中包含雨并且年龄小于40(需求1加强版)
	 第一种情况：  select id,name -- 就用select("")
	 from user
	 where name like '%雨%' and age<40
		 第二种情况：  select id,name,age,email
			 from user
			 where name like '%雨%' and age<40
		 */
	@Test
	public void setUserMapper11() {
		QueryWrapper<User> queryWrapper = Wrappers.<User>query();

		queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("manager_id") && !tableFieldInfo.getColumn().equals("create_time")).in("age", Arrays.asList(30, 32, 33));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 条件为真才加入到 where 条件中
	 */
	@Test
	public void selectByCondition() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		String name = "红";
		String email = "1";
		queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
				.like(StringUtils.isNotBlank(email), "email", email)
				.orderByDesc("id");

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void setUserMapperEntity() {
		User useWhere = new User();
		useWhere.setRealName("小红1");
		useWhere.setAge(32);

		QueryWrapper<User> queryWrapper = new QueryWrapper<>(useWhere);

		queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("manager_id") && !tableFieldInfo.getColumn().equals("create_time")).in("age", Arrays.asList(30, 32, 33));

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}


	@Test
	public void setUserMapperEntity1() {
		User useWhere = new User();
		useWhere.setRealName("小红1");	// 默认是等值的，比如要用 like 呢，在实体类中使用 @TableField condition 属性
		useWhere.setAge(32);

		QueryWrapper<User> queryWrapper = new QueryWrapper<>(useWhere);

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}
}
