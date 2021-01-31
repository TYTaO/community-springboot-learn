package com.tytao.community.service;

import com.tytao.community.mapper.UserMapper;
import com.tytao.community.model.User;
import com.tytao.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        if (dbUsers.size() == 0){
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            // 更新
            user.setGmtModified(System.currentTimeMillis());
            User updateUser = new User();
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setGmtModified(user.getGmtModified());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample userExampleUpdate = new UserExample();
            userExampleUpdate.createCriteria().andAccountIdEqualTo(user.getAccountId());
            userMapper.updateByExampleSelective(user, userExampleUpdate);
        }
    }
}
