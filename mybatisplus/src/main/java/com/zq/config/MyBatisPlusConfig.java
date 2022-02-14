package com.zq.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

//    // 新版已废弃，不起作用
//    @Bean
//    public PaginationInnerInterceptor paginationInnerInterceptor() {
//        return new PaginationInnerInterceptor();
//    }

    /**
     * 注册插件
     *
     * 依赖以下版本以上
     *
     * 		<dependency>
     * 			<groupId>com.baomidou</groupId>
     * 			<artifactId>mybatis-plus-boot-starter</artifactId>
     * 			<version>3.4.1</version>
     * 		</dependency>
     *
     * 分为 6 步：
     * 1. 创建一个拦截器
     * 2. 创建分页插件
     * 3. 设置请求的页面大于最大页后的操作，true 跳回到首页，false 继续请求。默认为 false
     * 4. 单页分页条数限制，默认无限制
     * 5. 设置数据库类型
     * 6. 添加内部拦截器
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1. 创建一个拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 2. 创建分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();

        // 3. 设置请求的页面大于最大页后的操作，true 跳回到首页，false 继续请求。默认为 false
        pageInterceptor.setOverflow(false);

        // 4. 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);

        // 5. 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);

        // 6. 添加内部拦截器
        interceptor.addInnerInterceptor(pageInterceptor);

        return interceptor;
    }
}
