package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.base.exception.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.MD5Util;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserSerivce {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user,String code,String correctCode) {

        //获取用户登录的ip
        String remoteIp = user.getAllowIps();

        //校验验证码
        if(!correctCode.equalsIgnoreCase(code)){
            throw new CrmException(CrmEnum.USER_LOGIN_CODE);
        }



        //密码加密
        user.setLoginPwd(MD5Util.getMD5(user.getLoginPwd()));

        //使用select方式
       /* user.setAllowIps(null);
        List<User> users = userMapper.select(user);*/

       //使用Example方式
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("loginAct", user.getLoginAct())
                .andEqualTo("loginPwd", user.getLoginPwd());
        List<User> users = userMapper.selectByExample(example);

        if(users.size() == 0){
            //用户不存在
            throw new CrmException(CrmEnum.USER_LOGIN_USERNAME_PASSWORD);
        }
        user = users.get(0);
        //账号是否实效
        String now = DateTimeUtil.getSysTime();
        String expireTime = user.getExpireTime();
        if(expireTime.compareTo(now) < 0){
            //账号失效
            throw new CrmException(CrmEnum.USER_LOGIN_EXPIRED);
        }

        //账号是否被所动
        if("0".equals(user.getLockState())){
            throw new CrmException(CrmEnum.USER_LOGIN_LOCKED);
        }

        //是否允许登录的ip
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(remoteIp)){
            throw new CrmException(CrmEnum.USER_LOGIN_ALLOWDIP);
        }
        return user;
    }

    @Override
    public void verifyOldPwd(String oldPwd, String correctPwd) {
        //对密码进行加密
        oldPwd = MD5Util.getMD5(oldPwd);
        if(!correctPwd.equals(oldPwd)){
            throw new CrmException(CrmEnum.USER_UPDATE_OLDPWD);
        }
    }

    @Override
    public void updateUser(User user) {

        //密码加密
        user.setLoginPwd(MD5Util.getMD5(user.getLoginPwd()));
        //count:影响的记录数
       int count = userMapper.updateByPrimaryKeySelective(user);
       if(count == 0){
           throw new CrmException(CrmEnum.USER_UPDATE_INFO);
       }
    }
}
