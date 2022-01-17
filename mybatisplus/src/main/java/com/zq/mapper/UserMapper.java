package com.zq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.entity.User;

/**
 *  BaseMapper 有一个泛型，就是你要操作的实体类
 */
public interface UserMapper extends BaseMapper<User> {
}
