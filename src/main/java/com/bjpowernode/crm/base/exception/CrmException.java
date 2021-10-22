package com.bjpowernode.crm.base.exception;

/**
 * Throwable
 */
public class CrmException extends RuntimeException {

    private CrmEnum crmEnum;

    public CrmException(CrmEnum crmEnum) {
        //调用父类的构造方法，调用该方法的目的是为了把错误信息放入到堆栈中
        super(crmEnum.getMessage());
        this.crmEnum = crmEnum;
    }
}
