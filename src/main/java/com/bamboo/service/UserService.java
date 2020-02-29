package com.bamboo.service;

import com.bamboo.pojo.User;

public interface UserService {

    User checkUser(String username, String password);
}
