package com.zq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
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
class MapperSelectTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void selectAll() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
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
	 * name like '%红%' and age<40
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
	 *  名字中包含小并且龄大于等于20且小于等于40并且email不为空
	 *  name like '%小%' and age between 20 and 40 and email is not null
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
	 *  name like '小%' or age>=25 order by age desc,id asc
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
	 * name like '小%' and (age<40 or email is not null)
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
	 * name like '小%' or (age<40 and age>20 and email is not null)
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

	@Test
	public void setUserMapperAllEq() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "小红");
		map.put("age", null);

		queryWrapper.allEq(map, false);

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void setUserMapperAllEq2() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "小红");
		map.put("age", 20);

		queryWrapper.allEq(StringUtils.isNotBlank(map.get("name").toString()), (k, v) -> k.equals("name"), map, false);

		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 使用场景1：不需要返回数据库全部字段，如果使用 selectList，则没有返回的字段会被设置成 null
	 */
	@Test
	public void selectByWrapperMaps1() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("id", "name").eq("age", 31);
		List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
		maps.forEach(System.out::println);
	}


	/**
	 * 使用场景2：在统计场景
	 *
	 *          *       按照直属上级分组，查询每组的 平均年龄、最小龄、最大年龄。
	 *          *           并且只取年龄总和小于500的组。
	 *          *               select
	 *          *                   avg(age) avg_age,
	 *          *                   min(age) min_age,
	 *          *                   max(age) max_age
	 *          *               from user
	 *          *               group by manager_id --上级id分组
	 *          *               having sum(age) <500 --只有总和小于500
	 *
	 */
	@Test
	public void selectByWrapperMaps2() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
				.groupBy("manager_id")
				.having("sum(age) < {0}", 500);

		List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
		maps.forEach(System.out::println);
	}

	/**
	 * 只返回第一列的值
	 */
	@Test
	public void selectByWrapperObjs() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("name", "红");

		List<Object> objects = userMapper.selectObjs(queryWrapper);
		objects.forEach(System.out::println);
	}


	/**
	 * 查询总记录数
	 */
	@Test
	public void selectByWrapperCount() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("name", "红");

		Integer count = userMapper.selectCount(queryWrapper);
		System.out.println(count);
	}

	/**
	 * 预期只返回一条记录的
	 */
	@Test
	public void selectByWrapperOne() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", "小张");

		User user = userMapper.selectOne(queryWrapper);
		System.out.println(user);
	}

	/**
	 * Lambda 条件构建器，优点：防止误写字段名
	 *
	 * 创建方式有三种：
	 * LambdaQueryWrapper<User> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
	 * LambdaQueryWrapper<User> lambdaQueryWrapper2 = new QueryWrapper<User>().lambda();
	 * LambdaQueryWrapper<User> lambdaQueryWrapper3 = Wrappers.<User>lambdaQuery();
	 *
	 *
	 * 方法引用四种形式
	 * 1. 类名::静态方法名
	 * 2. 对象::实例方法名
	 * 3. 类名::实例方法名    第一个参数来调用实例方法，其余参数作为方法的参数传递进去
	 * 4. 类名::new
	 */
	@Test
	public void selectLambda() {
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

		// where name '%红%' and age < 31
		lambdaQueryWrapper.like(User::getRealName, "红").le(User::getAge, 31);

		List<User> users = userMapper.selectList(lambdaQueryWrapper);

		users.forEach(System.out::println);	// 对象::实例方法名
	}

	/**
	 * 名字为王姓并且（年龄小于40 或 邮箱不为空）
	 * name like '王%' and (age<31 or email is not null)
	 */
	@Test
	public void selectLambda2() {
		LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

		lambdaQueryWrapper.likeRight(User::getRealName, "小").and(lqw -> lqw.lt(User::getAge, 31).or().isNotNull(User::getEmail));

		List<User> users = userMapper.selectList(lambdaQueryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * Lambda 链式调用方法
	 *
	 * 名字为王姓并且（年龄小于40 或 邮箱不为空）
	 * name like '小%' and (age<31 or email is not null)
	 */
	@Test
	public void selectLambda3() {
		// 泛型类，泛型构造器
		List<User> users = new LambdaQueryChainWrapper<User>(userMapper)
				.like(User::getRealName, "小")
				.and(lqw -> lqw.lt(User::getAge, 31).or().isNotNull(User::getEmail))
				.list();

		users.forEach(System.out::println);
	}

	/**
	 * 有时候只使用条件构造器不能满足我们的需求，这只我们要使用自定义 SQL 又要使用条件构造器
	 * 使用条件构造器的自定义 SQL
	 */
	@Test
	public void selectAllCustomize()
	{
		LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
		lambdaQueryWrapper.select(User::getId, User::getRealName, User::getAge).like(User::getRealName, "红");

		List<User> users = userMapper.selectAll(lambdaQueryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * 使用条件构造器的自定义 SQL
	 */
	@Test
	public void selectAllCustomize2()
	{
		LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
		lambdaQueryWrapper.select(User::getId, User::getRealName, User::getAge).like(User::getRealName, "红");

		List<User> users = userMapper.selectAll2(lambdaQueryWrapper);
		users.forEach(System.out::println);
	}

	/**
	 * mybatis 提供的是内存分页，它先把数据全查询出来然后全部加载到内存中，
	 * mp 提供了物理分页的插件，解决上述问题，既然是插件，肯定要配置
	 *
	 * BaseMapper 中提供了两个分页方法 selectPage  selectMpsPage
	 *
	 * 还有一个 IPage 接口提供了获取分页信息的方法（获取总记录数、总页数、当前页、每页多少条）
	 * IPage 还有一个实现类  Page
	 *
	 * selectPage 方法就需要两个参数，一个是 IPage 的实现类，加一个 Wrapper 类对象
	 */
	@Test
	public void selectPage() {
		LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();// 在调用方法的前面加入 <实参> 来显式指定泛型方法类型
		lambdaQueryWrapper.like(User::getRealName, "小").lt(User::getAge, 40);

		Page<User> page = new Page<User>(1, 2, true);
		IPage<User> iPage = userMapper.selectPage(page, lambdaQueryWrapper);

		System.out.println("总记录数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		System.out.println("每页多少条：" + iPage.getSize());
		System.out.println("当前页：" + iPage.getCurrent());

		List<User> users = iPage.getRecords();
		users.forEach(System.out::println);
	}

	/**
	 * 第二种使用  selectMapsPage，如果每一个参数是 Page<User> page = new Page<User>(1, 2);，此时 page 会报错
	 * 因为新版 3.4.1 之后，给他设定了具体类型，我们需要把  page 转成对应的类型 IPage<Map<String, Object>>
	 */
	@Test
	public void selectMapsPage() {
		LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
		lambdaQueryWrapper.like(User::getRealName, "小").lt(User::getAge, 40);

		Page<Map<String, Object>> page = new Page<>(1, 2);
		Page<Map<String, Object>> iPage = userMapper.selectMapsPage(page, lambdaQueryWrapper);

		System.out.println("总记录数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		System.out.println("每页多少条：" + iPage.getSize());
		System.out.println("当前页：" + iPage.getCurrent());

		List<Map<String, Object>> records = iPage.getRecords();

		records.forEach(System.out::println);
	}

	@Test
	public void selectMyPage() {
		LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
		lambdaQueryWrapper.like(User::getRealName, "小").lt(User::getAge, 40);

		Page<User> page = new Page<>(1, 2);

		IPage<User> iPage = userMapper.selectMyPage(page, lambdaQueryWrapper);

		System.out.println("总记录数：" + iPage.getTotal());
		System.out.println("总页数：" + iPage.getPages());
		System.out.println("每页多少条：" + iPage.getSize());
		System.out.println("当前页：" + iPage.getCurrent());

		iPage.getRecords().forEach(System.out::println);
	}
}
