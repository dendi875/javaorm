package com.zq.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 工具类，来创建 SqlSession 对象
 */
public class MyBatisUtil {

    private static SqlSessionFactory factory = null;

    static {
        // 1. 定义 mybatis 主配置文件的位置，路径是从类路径开始的相对路径
        String config = "mybatis/mybatis-config.xml";
        // 2. 读取主配置文件的信息，使用 mybatis 框架的 Resources 类
        try {
            InputStream inputStream = Resources.getResourceAsStream(config);
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        SqlSession session = null;
        if (factory != null) {
            session = factory.openSession();
        }
        return session;
    }
}
