package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.User;

public interface UserService {
    User findByUserName(String username);
    void updateAllPasswords();
}