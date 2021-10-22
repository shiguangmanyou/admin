package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.BarVo;
import com.bjpowernode.crm.workbench.bean.PieVo;
import com.bjpowernode.crm.workbench.bean.StageVo;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<String> queryCustomerName(String customerName);

    Map<String,Object> queryStages(String id, Integer index, Map<String,String> stage2PossibilityMap, User user);

    BarVo barVoEcharts();

    List<PieVo> pieVoEcharts();
}
