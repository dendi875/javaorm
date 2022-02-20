package com.zq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zq.entity.User;
import com.zq.mapper.UserMapper;
import com.zq.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
