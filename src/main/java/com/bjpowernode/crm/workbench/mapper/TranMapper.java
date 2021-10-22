package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.PieVo;
import com.bjpowernode.crm.workbench.bean.Tran;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TranMapper extends Mapper<Tran> {


    List<Map<String,Long>> queryStages();

    List<PieVo> queryPieVoStages();
}
