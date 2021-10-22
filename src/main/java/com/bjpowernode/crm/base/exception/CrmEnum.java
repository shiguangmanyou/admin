package com.bjpowernode.crm.base.exception;

import lombok.Data;

public enum CrmEnum {

    USER_LOGIN_USERNAME_PASSWORD("001-001-001","用户名或密码错误"),
    USER_LOGIN_CODE("001-001-002","验证码错误"),
    USER_LOGIN_EXPIRED("001-001-003","账号已失效"),
    USER_LOGIN_LOCKED("001-001-004","账号已失效"),
    USER_LOGIN_ALLOWDIP("001-001-005","不允许登录的ip"),
    USER_UPDATE_SUFFIX("001-002-001","只允许上传后缀名为jpg,png,webp,gif图片"),
    USER_UPDATE_SIZE("001-002-002","上传头像不能超过2M"),
    USER_UPDATE_OLDPWD("001-002-003","原始密码输入不正确"),
    USER_UPDATE_INFO("001-002-004","更新用户信息失败"),
    ACTIVITY_ADD("002-001","添加市场活动失败"),
    ACTIVITY_UPDATE("002-002","修改市场活动失败"),
    ACTIVITY_DELETE("002-003","删除市场活动失败"),
    CLUE_CONVERT("003-001","线索转换失败"),;
    private String code;//代表操作码，区分到底是因为操作什么模块的数据导致失败 001
    private String message;

    CrmEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
