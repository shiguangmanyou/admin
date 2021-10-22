package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.CommonUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * RestController/Controller
 * RestController：意味着类中的所有方法返回的都是json串,不用加@Response
 */
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;



   /* @RequestMapping("/test")
    public User test(){
        User user = new User();
        user.setName("hah");
        return user;
    }

    @RequestMapping("/test01")
    public String test01(){
        return "workbench/index";
    }*/

   @RequestMapping("/test01")
   public void test01(Integer age){
       //intValue null.intValue()
       if(age == 0){
           System.out.println("123");
       }
   }


   //异步查询所有市场活动数据
    @RequestMapping("/workbench/activity/list")
    public PageInfo list(int page,int pageSize,Activity activity){

        List<Activity> activities = activityService.list(page,pageSize,activity);

        PageInfo pageInfo = new PageInfo(activities);
        return pageInfo;
    }

    //异步查询所有者信息
    @RequestMapping("/workbench/activity/queryUsers")
    public List<User> queryUsers(){
       return activityService.queryUsers();
    }

    //异步保存市场活动
    @RequestMapping("/workbench/activity/saveOrUpdate")
    public ResultVo saveOrUpdate(Activity activity, HttpSession session){
       ResultVo resultVo = new ResultVo();
       try{
           User user = CommonUtil.getCurrentUser(session);
           resultVo = activityService.saveOrUpdate(activity,user);
       }catch (CrmException e){
           resultVo.setMessage(e.getMessage());
       }
       return resultVo;
    }

    //根据主键查询数据
    @RequestMapping("/workbench/activity/queryById")
    public Activity queryById(String id){
       return activityService.queryById(id);
    }

    //批量删除
    @RequestMapping("/workbench/activity/deleteBatch")
    public ResultVo deleteBatch(String ids){
        ResultVo resultVo = new ResultVo();
        try{
           activityService.deleteBatch(ids);
           resultVo.setOk(true);
           resultVo.setMessage("删除市场活动成功");
        }catch (CrmException e){
            e.printStackTrace();
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //跳转到市场活动详情页
    @RequestMapping("/workbench/activity/toDetail")
    public Activity toDetail(String id){
       return activityService.toDetail(id);
    }

    //异步保存备注数据
    @RequestMapping("/workbench/activity/saveRemark")
    public ResultVo saveRemark(ActivityRemark activityRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = CommonUtil.getCurrentUser(session);
            activityService.saveRemark(activityRemark,user);
            resultVo.setOk(true);
            resultVo.setMessage("添加备注成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
       return resultVo;
    }

    //异步修改备注
    @RequestMapping("/workbench/activity/updateRemark")
    public ResultVo updateRemark(ActivityRemark activityRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = CommonUtil.getCurrentUser(session);
            activityService.updateRemark(activityRemark,user);
            resultVo.setOk(true);
            resultVo.setMessage("修改备注成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //异步删除备注
    @RequestMapping("/workbench/activity/deleteRemark")
    public ResultVo deleteRemark(String id){
        ResultVo resultVo = new ResultVo();
        try{
            activityService.deleteRemark(id);
            resultVo.setOk(true);
            resultVo.setMessage("删除备注成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }
}
