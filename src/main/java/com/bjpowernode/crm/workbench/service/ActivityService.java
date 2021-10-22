package com.bjpowernode.crm.workbench.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;

import java.util.List;

public interface ActivityService {
    List<Activity> list(int page,int pageSize,Activity activity);

    List<User> queryUsers();

    ResultVo saveOrUpdate(Activity activity, User user);

    Activity queryById(String id);

    void deleteBatch(String ids);

    Activity toDetail(String id);

    void saveRemark(ActivityRemark activityRemark,User user);

    void updateRemark(ActivityRemark activityRemark, User user);

    void deleteRemark(String id);

    ExcelWriter exportExcel();
}
