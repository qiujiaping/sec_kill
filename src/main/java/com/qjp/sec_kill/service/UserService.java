package com.qjp.sec_kill.service;

import com.qjp.sec_kill.dao.UserDao;
import com.qjp.sec_kill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }


    public int insertUser(User user) {
        int insert = userDao.insert(user);
        return insert;
    }

    public User getByName(String name) {
        return userDao.getByName(name);
    }
}
