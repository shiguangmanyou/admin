package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.CommonUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;
import com.bjpowernode.crm.workbench.bean.Tran;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;

    //跳转到线索详情页
    @RequestMapping("/workbench/clue/toDetail")
    @ResponseBody
    public Clue toDetail(String id){
        return clueService.toDetail(id);
    }

    //查询满足条件的市场活动
    @RequestMapping("/workbench/clue/queryActivities")
    @ResponseBody
    public List<Activity> queryActivities(String id,String name){
        return clueService.queryActivities(id,name);
    }

    //绑定市场活动
    @RequestMapping("/workbench/clue/bind")
    @ResponseBody
    public ResultVo bind(String ids,String id){
        ResultVo resultVo = new ResultVo();
        try{
            resultVo = clueService.bind(ids,id);
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //解除绑定市场活动
    @RequestMapping("/workbench/clue/unbind")
    @ResponseBody
    public ResultVo unbind(ClueActivityRelation clueActivityRelation){
        ResultVo resultVo = new ResultVo();
        try{
            clueService.unbind(clueActivityRelation);
            resultVo.setOk(true);
            resultVo.setMessage("解绑成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //根据主键查询线索
    @RequestMapping("/workbench/clue/queryById")
    @ResponseBody
    public Clue queryById(String id){
        return clueService.queryById(id);
    }

    //线索转换
    @RequestMapping("/workbench/clue/convert")
    @ResponseBody
    public ResultVo convert(String id, String isCreateTransaction, Tran tran,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = CommonUtil.getCurrentUser(session);
            clueService.convert(id,isCreateTransaction,tran,user);
            resultVo.setOk(true);
            resultVo.setMessage("转换成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //点击搜索线索转换中关联过的市场活动
    @RequestMapping("/workbench/clue/searchActivity")
    @ResponseBody
    public List<Activity> searchActivity(String id,String name){
        return clueService.searchActivity(id,name);
    }
}
