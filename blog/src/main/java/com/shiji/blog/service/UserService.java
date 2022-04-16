package com.shiji.blog.service;

import com.shiji.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);
}
