package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.util.CommonUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.StageVo;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
public class TranController {

    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/transaction/queryCustomerName")
    public List<String> queryCustomerName(String customerName){
        return tranService.queryCustomerName(customerName);
    }

    //查询阶段对应的可能性
    @RequestMapping("/workbench/tran/stage2Possibility")
    public String stage2Possibility(String stage,HttpSession session){
        Map<String,String> stage2PossibilityMap =
                (Map<String, String>) session.getServletContext().getAttribute("stage2PossibilityMap");
        return stage2PossibilityMap.get(stage);
    }

    //异步查询交易图标
    @RequestMapping("/workbench/tran/queryStages")
    public Map<String,Object> queryStages(String id,Integer index,HttpSession session){
        Map<String,String> stage2PossibilityMap =
                (Map<String, String>) session.getServletContext().getAttribute("stage2PossibilityMap");

        //获取当前登录用户
        User user = CommonUtil.getCurrentUser(session);
       Map<String,Object> map = tranService.queryStages(id,index,stage2PossibilityMap,user);

        return map;
    }
}
