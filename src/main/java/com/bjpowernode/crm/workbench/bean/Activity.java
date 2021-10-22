package com.bjpowernode.crm.workbench.bean;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "tbl_activity")
@NameStyle(Style.normal)
public class Activity {

    @Id
    private String id;//主键
    private String owner;//用户的外键
    private String name;
    private String startDate;
    private String endDate;
    private String cost;//花费
    private String description;
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;

    //备注
    private List<ActivityRemark> activityRemarks;

   // private List<Clue> clues;

}
