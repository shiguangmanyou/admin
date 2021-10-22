package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.bean.User;

public interface UserSerivce {
    User login(User user,String code,String correctCode);

    void verifyOldPwd(String oldPwd, String correctPwd);

    void updateUser(User user);
}
