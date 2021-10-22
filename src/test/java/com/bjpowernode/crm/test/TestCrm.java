package com.bjpowernode.crm.test;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import com.bjpowernode.crm.base.exception.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.MD5Util;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestCrm {

    BeanFactory beanFactory =
            new ClassPathXmlApplicationContext("Spring/applicationContext.xml");
    UserMapper userMapper = (UserMapper) beanFactory.getBean("userMapper");
    //测试添加
    @Test
    public void test01(){

        User user = new User();
        user.setId("444");
        user.setLoginAct("zhangsan");
        user.setName("二狗子");

        //insertSelective:只插入有值的数据 类似动态sql中if判断 <if test="name != null and name !=''">
        //userMapper.insertSelective(user);
        userMapper.insert(user);
    }

    //测试删除
    @Test
    public void test02(){
        //根据主键删除
        //userMapper.deleteByPrimaryKey("444");

        //根据条件删除,该种方式只能做等值的条件判断
       /* User user = new User();
        user.setLoginAct("zhangsan");
        userMapper.delete(user);*/

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        //%:0-n个字符 _:1个字符
        //参数1:实体类对应的属性名 参数2:实际的参数
      /*  String loginAct = "x";
        criteria.andLike("loginAct", "%" + loginAct + "%");
        userMapper.deleteByExample(example);*/

      //批量删除 delete from tbl_user where id in(1,2,3)
        List<String> ids = new ArrayList<>();
        ids.add("b7658115358d4f3ea22f31604d721a18");
        ids.add("b7658115358d4f3ea22f31604d721a19");
        criteria.andIn("id", ids);
        userMapper.deleteByExample(example);
    }

    //更新
    @Test
    public void test03(){
        User user = new User();
        user.setLoginAct("张三");
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email", "26559041021@qq.com");
        userMapper.updateByExample(user,example);
    }

    //<select id parameterType resultType/resultMap>
    //查询
    @Test
    public void test04(){
        //把条件封装到User对象中，返回List<User>
//        userMapper.select();
        //查询用户表所有数据
        //userMapper.selectAll();
        //根据Example拼接条件，返回List<User>
       // userMapper.selectByExample();
        //实体类添加@Id注解，根据主键查询
        //userMapper.selectByPrimaryKey();
        User user = new User();
        user.setId("b7658115358d4f3ea22f31604d721a19");
        User user1 = userMapper.selectOne(user);

        System.out.println(user1);

        //排序 order by
        Example example = new Example(User.class);
        example.setOrderByClause("expireTime desc");
        List<User> users = userMapper.selectByExample(example);

        for (User user2 : users) {
            System.out.println(user2);
        }
    }

    //测试主键
    @Test
    public void test05(){
        String s = UUID.randomUUID().toString().replace("-", "");
        System.out.println(s);
    }

    //密码加密 加密算法:md5

    /**
     * md5加密+盐(干扰作用)
     */
    //md5(md5(md5("admin")))
    @Test
    public void test06(){
        String admin = MD5Util.getMD5("123");
        System.out.println(admin);
    }

    //测试自定义异常
    @Test
    public void test07(){
        int a = 0;
        try{
            if(a == 0){
                throw new CrmException(CrmEnum.USER_LOGIN_USERNAME_PASSWORD);
            }
        }catch (CrmException e){

            System.out.println(e.getMessage());
        }

    }

    //测试图片验证码
    @Test
    public void test08(){
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 10);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("d:/circle.png");
    }

    //字符串日期大小比较

    /**
     * expireTime < now : < 0
     * expireTime == now : = 0
     * expireTime > now : >0
     */
    @Test
    public void test09(){
        String expireTime = "2032-09-28";
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        System.out.println(expireTime.compareTo(now));
    }
}
