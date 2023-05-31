package com.run.api.domain.users.service;

import com.run.api.global.dao.UserMapper;
import com.run.api.global.dto.user;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServicelmpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public List<user> getUserList() {
        return userMapper.getUserList();
    }

}