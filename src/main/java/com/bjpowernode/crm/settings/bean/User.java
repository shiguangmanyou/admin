package com.bjpowernode.crm.settings.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_user")
@NameStyle(Style.normal)
public class User {

    @Id
    private String id;//主键
    private String loginAct;//登录账号
    private String name;//昵称
    private String loginPwd;//登录密码
    private String email;
    private String expireTime;//失效时间 yyyy-MM-dd hh:mm:ss
    private String lockState;//账号是否被锁定 0:被锁定 1:没有锁定
    private String deptno;
    private String allowIps;//允许登录系统的ip地址，只允许公司内部机器登录
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String img;//头像 clob blob 数据库存储的是图片路径 /crm/image/home.png

}
