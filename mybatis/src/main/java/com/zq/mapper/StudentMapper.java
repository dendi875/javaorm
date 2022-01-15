package com.zq.mapper;

import com.zq.entity.Student;
import com.zq.vo.CustomObject;
import com.zq.vo.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    Student getOne(Integer id);

    List<Student> getAll();

    int insert(Student student);


    Student selectStudentById(Integer id);

    /**
     * 一个简单类型的参数：
     *   简单类型： mybatis把java的基本数据类型和String都叫简单类型。
     *  在mapper文件获取简单类型的一个参数的值，使用 #{任意字符}
     */
    Student selectByEmail(String email);

    /**
     * 如果有多个简单参数：
     * 需要使用 @Param 注解，在方法的形参最前面加入 @Param("自定义参数名称")，这个名称可以用在你的 mapper xml 文件中
     */
    List<Student> selectMultiParam(@Param("myname") String name, @Param("myage") Integer age);

    /**
     * 使用 java 对象作为形参，
     * 只要这个对象有属性，每个属性有 set，get 方法就可以使用
     */
    List<Student> selectMultiStudent(Student student);

    /**
     * 我们不使用 Student 对象，我们使用其它对象
     */
    List<Student> selectByQueryParam(QueryParam queryParam);

    /**
     * 按位置传参
     */
    List<Student> selectByArg(String name, Integer age);

    /**
     * 使用 Map 作为参数
     */
    List<Student> selectByMap(Map<String, Object> map);

    /**
     * ${} 的使用，把查询出来的数据按 指定列名 排序
     */
    List<Student> selectStudentOrderByColName(@Param("tableName") String table, @Param("colName") String name);

    Student selectTypeAliasesById(Integer id);

    /**
     * resultType 的值不一定就必须是实体类对象，它就是一个自定义对象
     */
    CustomObject selectCustomObjectById(@Param("cid") Integer id);

    /**
     * resultType 返回一个简单类型，比如统计行数，算平均值等
     */
    Long count();

    /**
     * resultType 返回一个 Map 结构数据
     */
    Map<Object, Object> selectReturnMap(Integer id);

    /**
     * 使用 resultMap 来解决列名和属性名不一样的问题
     */
    CustomObject selectResultMap(@Param("sid") Integer id);

    /**
     * 使用 resultType + as 来解决列名和属性名不一样的问题
     */
    CustomObject selectResultTypeCustomObject(@Param("sid") Integer id);

    /**
     * like 查询的第一种方式
     */
    List<Student> selectLikeFirst(String email);

    /**
     * like 查询的第二种方式，"%" #{xx} "%"
     */
    List<Student> selectLikeSecond(String email);


    /**
     * 动态 sql if
     */
    List<Student> selectIf(Student student);

    /**
     * 动态 sql where
     */
    List<Student> selectWhere(Student student);

    /**
     * 动态 sql  foreach
     */
    List<Student> selectForList(List<Integer> idList);

    /**
     * 动态 sql  foreach
     */
    List<Student> selectForList2(List<Student> students);

    /**
     * sql 代码片段
     */
    List<Student> selectSql(List<Student> students);

    /**
     * sql 代码片段
     */
    List<Student> selectSql2(List<Student> students);


}
