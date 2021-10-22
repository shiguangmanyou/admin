package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;
import com.bjpowernode.crm.workbench.bean.Tran;

import java.util.List;

public interface ClueService {
    Clue toDetail(String id);

    List<Activity> queryActivities(String id,String name);

    ResultVo bind(String ids, String id);

    void unbind(ClueActivityRelation clueActivityRelation);

    Clue queryById(String id);

    void convert(String id, String isCreateTransaction, Tran tran,User user);

    List<Activity> searchActivity(String id,String name);
}
