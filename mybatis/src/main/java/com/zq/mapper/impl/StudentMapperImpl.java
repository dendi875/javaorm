package com.zq.mapper.impl;

import com.zq.entity.Student;
import com.zq.mapper.StudentMapper;
import com.zq.utils.MyBatisUtil;
import com.zq.vo.CustomObject;
import com.zq.vo.QueryParam;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student getOne(Integer id) {
        // 1. 创建 SqlSession 对象
        SqlSession session = MyBatisUtil.getSqlSession();
        // 2. 构建 sqlId
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "getOne";
        // 3. 使用 SqlSession 类中的方法执行 sql
        Student student = session.selectOne(sqlId, id);
        // 关闭 sqlSession
        session.close();

        return student;
    }

    @Override
    public List<Student> getAll() {
        // 创建 SqlSession 对象
        SqlSession session = MyBatisUtil.getSqlSession();
        // 创建 sqlId
        String sqlId = "com.zq.mapper.StudentMapper" + "." + "getAll";
        // 执行 sql
        List<Student> students = session.selectList(sqlId);
        session.close();

        return students;
    }

    @Override
    public int insert(Student student) {
        return 0;
    }

    @Override
    public Student selectStudentById(Integer id) {
        return null;
    }

    @Override
    public Student selectByEmail(String email) {
        return null;
    }

    @Override
    public List<Student> selectMultiParam(String name, Integer age) {
        return null;
    }

    @Override
    public List<Student> selectMultiStudent(Student student) {
        return null;
    }

    @Override
    public List<Student> selectByQueryParam(QueryParam queryParam) {
        return null;
    }

    @Override
    public List<Student> selectByArg(String name, Integer age) {
        return null;
    }

    @Override
    public List<Student> selectByMap(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<Student> selectStudentOrderByColName(String email, String name) {
        return null;
    }

    @Override
    public Student selectTypeAliasesById(Integer id) {
        return null;
    }

    @Override
    public CustomObject selectCustomObjectById(Integer id) {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public Map<Object, Object> selectReturnMap(Integer id) {
        return null;
    }

    @Override
    public CustomObject selectResultMap(Integer id) {
        return null;
    }

    @Override
    public CustomObject selectResultTypeCustomObject(Integer id) {
        return null;
    }

    @Override
    public List<Student> selectLikeFirst(String name) {
        return null;
    }

    @Override
    public List<Student> selectLikeSecond(String name) {
        return null;
    }

    @Override
    public List<Student> selectIf(Student student) {
        return null;
    }

    @Override
    public List<Student> selectWhere(Student student) {
        return null;
    }

    @Override
    public List<Student> selectForList(List<Integer> idList) {
        return null;
    }

    @Override
    public List<Student> selectForList2(List<Student> students) {
        return null;
    }

    @Override
    public List<Student> selectSql(List<Student> students) {
        return null;
    }

    @Override
    public List<Student> selectSql2(List<Student> students) {
        return null;
    }


}
