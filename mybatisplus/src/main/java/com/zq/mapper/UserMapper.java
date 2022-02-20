package com.zq.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zq.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  BaseMapper 有一个泛型，就是你要操作的实体类，需要操作的实体 mapper
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 使用条件构造器的自定义SQL，有两种方式
     * 方法一、
     * 在 mapper 接口中自定义，参考  List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
     * 参数的注解是固定的 ew 就是WRAPPER的值，ew 是固定的
     *
     * 注意：这里不用加 where
     *
     * @return
     */
    @Select("select * from mp_user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);


    /**
     * 使用条件构造器的自定义SQL，有两种方式
     * 方法二、看 selectAll2
     * 写在 mapper xml 中，在配置文件中配置接口对应的 mapper xml 文件
     * 1. 设置扫描路径 mybatis-plus.mapper-locations=mapper/*.xml
     * 2. 在resources下建mapper包，创建xml文件 UserMapper.xml
     *
     * @return
     */
    List<User> selectAll2(@Param(Constants.WRAPPER) Wrapper<User> wrapper);


    /**
     *
     * 自定义分页方法
     *
     * 在实际开发中有多表联查分页情况，那就不能使用  selectPage、selectMapsPage 来进行分页查询
     *
     * 怎么办？可以通过 xml 方式自定义分页查询方法，方法的定义模仿一下 BaseMapper 中的 selectPage
     *
     * <E extends IPage<T>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
     *
     * 2. xml 中配置
     */
     IPage<User> selectMyPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
