package com.bjpowernode.crm.workbench.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "tbl_clue")
@NameStyle(Style.normal)
public class Clue {

    @Id
    private String id;
    private String fullname;//联系人全名
    private String appellation;//联系人称呼
    private String owner;//所有者外键
    private String company;//公司名称
    private String job;
    private String email;
    private String phone;//公司座机
    private String website;
    private String mphone;//联系人手机
    private String state;//线索状态
    private String source;//线索来源
    private String createBy;
    private String createTime;
    private String editBy;
    private String editTime;
    private String description;
    private String contactSummary;
    private String nextContactTime;
    private String address;//地址:联系人


    private List<Activity> activities;//线索关联的市场活动

}
