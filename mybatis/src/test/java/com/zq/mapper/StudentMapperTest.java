package com.zq.mapper;

import com.github.pagehelper.PageHelper;
import com.zq.entity.Student;
import com.zq.mapper.impl.StudentMapperImpl;
import com.zq.utils.MyBatisUtil;
import com.zq.vo.CustomObject;
import com.zq.vo.QueryParam;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StudentMapperTest {

    /**
     * mybatis 核心类 SqlSessionFactory
     */
    @Test
    public void getOne() {
        // 1. 定义 mybatis 主配置文件的位置，路径是从类路径开始的相对路径
        String resource = "mybatis/mybatis-config.xml";
        // 2. 读取主配置文件的信息，使用 mybatis 框架的 Resources 类
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 创建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 4. 从 SqlSessionFactory 中获取 SqlSession
        // 5. 指定要执行的 sql 语句的 id（唯一标识） ，sql 的 id = namespace + "." + select | update | insert | delete 标签的 id 属性值
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "getOne"; // 哪个 mapper 文件的哪个 sql 语句

        // 6. 通过 SqlSession 类的方法，执行 sql 语句
        SqlSession session = sqlSessionFactory.openSession();
        Student student = session.selectOne(sqlId);

        System.out.println("student: " + student);

        // 7. 关闭 SqlSession 对象
        session.close();
    }


    /**
     * mybatis 核心类 SqlSessionFactory
     */
    @Test
    public void getOneSelectOne() {
        // 1. 定义 mybatis 主配置文件的位置，路径是从类路径开始的相对路径
        String resource = "mybatis/mybatis-config.xml";
        // 2. 读取主配置文件的信息，使用 mybatis 框架的 Resources 类
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 创建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 4. 从 SqlSessionFactory 中获取 SqlSession
        // 5. 指定要执行的 sql 语句的 id（唯一标识） ，sql 的 id = namespace + "." + select | update | insert | delete 标签的 id 属性值
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "getOne";

        // 6. 通过 SqlSession 类的方法，执行 sql 语句
        SqlSession session = sqlSessionFactory.openSession();
        Student student = session.selectOne(sqlId, 2);

        System.out.println("student: " + student);

        // 7. 关闭 SqlSession 对象
        session.close();
    }

    @Test
    public void insert() {
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 4. 从 SqlSessionFactory 中获取 SqlSession
        // 5. 指定要执行的 sql 语句的 id（唯一标识） ，sql 的 id = namespace + "." + select | update | insert | delete 标签的 id 属性值
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "insert";

        // 6. 通过 SqlSession 类的方法，执行 sql 语句
        SqlSession session = sqlSessionFactory.openSession();
        int id = session.insert(sqlId, new Student("小红", "7@qq.com", 30));
        session.commit();

        System.out.println("id: " + id);

        // 7. 关闭 SqlSession 对象
        session.close();
    }

    @Test
    public void getAll() {
        // 创建 SqlSession 对象
        SqlSession session = MyBatisUtil.getSqlSession();
        // 创建 sqlId
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "getAll";
        List<Student> students = session.selectList(sqlId);
        for (Student ele : students) {
            System.out.println(ele);
        }
        session.close();
    }

    /**
     * 通过 Mapper 的实现类来执行 sql
     *
     * 分析这行代码
     * List<Student> students = studentMapper.getAll();
     *
     * 1. 通过反射能获取 studentMapper 全限定名称
     * studentMapper 是 StudentMapper 类型的，StudentMapper 的全限定名称是 com.zq.mapper.StudentMapper
     * 2. getAll 方法是 StudentMapper中的方法名称，这个方法也是 mapper xml 文件中的标签 id
     * 通过 studentMapper.getAll() 可以得到 sqlId = "com.zq.mapper.StudentMapper" + "." + "getAll";
     * 3. 确定调用SqlSession中的哪个方法
     *   3.1. 根据 StudentMapper 接口中的方法返回值，如果返回的是一个对象，例如 Student，调用 SqlSession.selectOne()
     *   如果返回的是 List，例如  List<Student>，调用 SqlSession.selectList()
     *   3.2. 根据 mapper 文件中的标识，如果是 <insert> 标签，调用 SqlSession.insert() 方法
     *
     * mybatis 框架就是通过使用 xxxMapper 类中的方法调用来确定执行  sql 语句前的必要信息，mybatis 简化了 mapper 对象的实现
     * 由 mybatis 框架在程序执行期间，根据 mapper 接口，在内存中创建一个接口的实现类对象，mybatis 把这个技术称为动态代理
     *
     */
    @Test
    public void getAll2() {
        StudentMapper studentMapper = new StudentMapperImpl();
        List<Student> students = studentMapper.getAll();
        students.forEach((student) -> System.out.println(student));
    }

    /**
     * 使用 mybatis 的动态代理机制，使用 SqlSession.getMapper(mapper接口)
     * getMapper 能获取 mapper 接口对应的实现类对象,
     *
     * 使用代理要求：
     * 1. mapper 文件中的 namespace 必须是 mapper 接口的全限定名称
     * 2. mapper 文件中标签的 id 是 mapper 接口的方法名称
     */
    @Test
    public void getAll3() {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        System.out.println("studentMapper = " + studentMapper.getClass().getName());

        List<Student> students = studentMapper.getAll();
        students.forEach(System.out::println);
    }

    /**
     * 测试 xml 文件中的使用  parameterType 来定义参数类型
     */
    @Test
    public void selectStudentById()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Student student = studentMapper.selectStudentById(5);
        session.close();
        System.out.println(student);
    }

    /**
     * 测试简单参数 string 类型
     */
    @Test
    public void selectByEmail()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Student student = studentMapper.selectByEmail("1@qq.com");
        session.close();
        System.out.println(student);
    }

    /**
     * 测试多个简单参数情况下，我们要使用 @Param 注解
     */
    @Test
    public void selectMultiParam()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectMultiParam("张三", 23);
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * 测试当 java 对象作为形参的时候
     */
    @Test
    public void selectMultiStudent()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectMultiStudent(new Student("张三", "", 23));
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * 测试当使用 其它对象作为形参的时候
     */
    @Test
    public void selectByQueryParam()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        QueryParam queryParam = new QueryParam();
        queryParam.setP1("张三");
        queryParam.setP2("23");
        List<Student> students = studentMapper.selectByQueryParam(queryParam);
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * 测试当使用 按位置传参
     */
    @Test
    public void selectByArg()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectByArg("张三", 23);
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * 测试使用 Map 作为参数
     */
    @Test
    public void selectByMap()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 23);
        List<Student> students = studentMapper.selectByMap(map);
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * ${} 占位符的使用场景，一般作为表名，列名是使用
     */
    @Test
    public void selectStudentOrderByColName()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectStudentOrderByColName("student","age");
        session.close();
        students.forEach((student) -> {
            System.out.println(student);
        });
    }

    /**
     * 别名
     */
    @Test
    public void selectTypeAliasesById()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Student student = studentMapper.selectTypeAliasesById(1);
        System.out.println(student);
    }

    /**
     * resultType 就是一个自定义对象，不一定必须是实体类对象
     */
    @Test
    public void selectCustomObjectById()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        CustomObject customObject = studentMapper.selectCustomObjectById(1);
        System.out.println(customObject);
    }

    /**
     * resultType 返回一个简单类型
     */
    @Test
    public void count()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Long total = studentMapper.count();
        System.out.println(total);
    }

    /**
     * resultType 返回一个 Map
     */
    @Test
    public void selectReturnMap()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Map<Object, Object> map = studentMapper.selectReturnMap(1);
        System.out.println(map);
    }

    /**
     * 使用 resultMap 来解决列名和属性名不一样的问题
     */
    @Test
    public void selectResultMap()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        CustomObject customObject = studentMapper.selectResultMap(1);
        System.out.println(customObject);
    }

    /**
     * 使用 resultType + as 来解决列名和属性名不一样的问题
     */
    @Test
    public void selectResultTypeCustomObject()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        CustomObject customObject = studentMapper.selectResultTypeCustomObject(1);
        System.out.println(customObject);
    }

    /**
     * like 查询的第一种方式
     */
    @Test
    public void selectLikeFirst()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectLikeFirst("%qq.com%");
        students.forEach(student -> System.out.println(student));
    }

    /**
     * like 查询的第二种方式
     */
    @Test
    public void selectLikeSecond()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        List<Student> students = studentMapper.selectLikeSecond("qq.com");
        students.forEach(student -> System.out.println(student));
    }

    /**
     * 动态 SQL - if
     */
    @Test
    public void selectIf()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Student student = new Student();
        student.setEmail("%qq.com%");
        student.setAge(23);
        List<Student> students = studentMapper.selectIf(student);
        students.forEach(ele -> System.out.println(ele));
    }

    /**
     * 动态 SQL - where
     */
    @Test
    public void selectWhere()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);
        Student student = new Student();
        student.setEmail("%qq.com%");
        student.setAge(23);
        List<Student> students = studentMapper.selectWhere(student);
        students.forEach(ele -> System.out.println(ele));
    }

    /**
     * 动态 SQL - foreach
     */
    @Test
    public void selectForList()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        idList.add(3);

        List<Student> students = studentMapper.selectForList(idList);
        students.forEach(ele -> System.out.println(ele));
    }

    /**
     * 动态 SQL - foreach
     */
    @Test
    public void selectForList2()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1);

        Student student2 = new Student();
        student2.setId(2);

        students.add(student1);
        students.add(student2);

        List<Student> results = studentMapper.selectForList2(students);
        results.forEach(ele -> System.out.println(ele));
    }

    @Test
    public void selectSql()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1);

        Student student2 = new Student();
        student2.setId(2);

        students.add(student1);
        students.add(student2);

        List<Student> results = studentMapper.selectSql(students);
        results.forEach(ele -> System.out.println(ele));
    }

    @Test
    public void selectSql2()
    {
        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Student> students = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1);

        Student student2 = new Student();
        student2.setId(2);

        students.add(student1);
        students.add(student2);

        List<Student> results = studentMapper.selectSql2(students);
        results.forEach(ele -> System.out.println(ele));
    }

    @Test
    public void testPageHelper() {
        PageHelper.startPage(2, 2);

        SqlSession session = MyBatisUtil.getSqlSession();
        StudentMapper studentMapper = session.getMapper(StudentMapper.class);

        List<Student> students = studentMapper.getAll();
        students.forEach(ele -> System.out.println(ele));
    }
}