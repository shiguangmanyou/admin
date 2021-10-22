package com.bjpowernode.crm.base.bean;

import lombok.Data;

/**
 * 给客户端响应结果的对象
 * Ajax:XmlHttpRequest
 */
@Data
public class ResultVo<T> {

    private boolean isOk;//代表当前用户是否操作成功
    private String message;//给客户端返回的消息
    private T t;//返回的数据

}
