package com.bjpowernode.crm.workbench.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_tran")
@NameStyle(Style.normal)
public class Tran {

    @Id
    private String id;
    private String owner;//所有者外键
    private String money;
    private String name;
    private String expectedDate;
    private String customerId;//客户的外键
    private String stage;//阶段
    private String possibility;//可能性
    private String type;//类型
    private String source;//来源
    private String activityId;//市场活动外键
    private String contactsId;
    private String createBy;
    private String createTime;
    private String editBy;
    private String editTime;
    private String description;
    private String contactSummary;
    private String nextContactTime;

}
