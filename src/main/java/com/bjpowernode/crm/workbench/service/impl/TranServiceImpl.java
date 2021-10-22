package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {


    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;


    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<String> queryCustomerName(String customerName) {
        Example example = new Example(Customer.class);
        example.createCriteria().andLike("name", "%" + customerName + "%");
        List<Customer> customers = customerMapper.selectByExample(example);

        //定义一个集合存储客户名称
        List<String> names = new ArrayList<>();
        for (Customer customer : customers) {
            names.add(customer.getName());
        }
        return names;
    }

    @Override
    public Map<String,Object> queryStages(String id, Integer index1, Map<String,String> stage2PossibilityMap, User user) {

        Map<String, Object> map = new HashMap<>();

        //查询当前交易所处的阶段和可能性
        Tran tran = tranMapper.selectByPrimaryKey(id);
        //获取交易阶段
        String currentStage = "";
        String currentPossibility = "";
        //要把Map转换成List集合
        List<Map.Entry<String,String>> stages = new ArrayList<>(stage2PossibilityMap.entrySet());

        if(index1 != null){
            //点击修改
            currentStage = stages.get(index1).getKey();
            currentPossibility = stages.get(index1).getValue();

            tran.setStage(currentStage);
            tran.setPossibility(currentPossibility);
            //修改交易阶段和可能性
            tranMapper.updateByPrimaryKeySelective(tran);

            //添加一条交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(currentStage);
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setCreateBy(user.getName());
            tranHistory.setTranId(id);
            tranHistory.setPossibility(currentPossibility);

            tranHistoryMapper.insertSelective(tranHistory);
            map.put("tranHistory", tranHistory);
            map.put("index", index1);

        }else{
            //第一次查询
            currentStage = tran.getStage();
            currentPossibility = tran.getPossibility();
        }

        //记录锚点位置
        int index = 0;
        int position = 0;
        for(int i = 0; i < stages.size(); i++){
            String stage = stages.get(i).getKey();
            String possibility = stages.get(i).getValue();
            if(stage.equals(currentStage)){
                index = i;
            }
            if("0".equals(possibility)){
                //第一个可能性为0阶段
                position = i;
                break;
            }
        }

        //定义一个集合存储返回给前台的图标对象集合
        List<StageVo> stageVos = new ArrayList<>();
        if("0".equals(currentPossibility)){
            //交易失败
            for(int i = 0; i < stages.size(); i++) {
                StageVo stageVo = new StageVo();
                String stage = stages.get(i).getKey();
                String possibility = stages.get(i).getValue();
                if("0".equals(possibility)){
                    if(stage.equals(currentStage)){
                        stageVo.setType("红x");
                    }else{
                        stageVo.setType("黑x");
                    }
                }else{
                    stageVo.setType("黑圈");
                }
                stageVo.setContent(stage + ":" + possibility);
                stageVos.add(stageVo);
            }

        }else{
            //交易中 推出右边两个黑x
            for(int i = 0; i < stages.size(); i++) {
                StageVo stageVo = new StageVo();
                String stage = stages.get(i).getKey();
                String possibility = stages.get(i).getValue();
                if(i < index){
                    stageVo.setType("绿圈");
                }else if(i == index){
                    stageVo.setType("锚点");
                }else if(i > index && i < position){
                    stageVo.setType("黑圈");
                }else{
                    stageVo.setType("黑x");
                }
                stageVo.setContent(stage + ":" + possibility);
                stageVos.add(stageVo);
            }
        }
        map.put("stageVos", stageVos);
        return map;
    }

    @Override
    public BarVo barVoEcharts() {
        List<Map<String, Long>> maps = tranMapper.queryStages();
        BarVo barVo = new BarVo();

        //标题的集合
        List<String> titles = new ArrayList<>();
        //数据的集合
        List<Long> values = new ArrayList<>();
        for (Map<String, Long> map : maps) {
            titles.add(map.get("stage") + "");
            values.add(map.get("num"));
        }
        barVo.setTitles(titles);
        barVo.setValues(values);
        return barVo;
    }

    @Override
    public List<PieVo> pieVoEcharts() {
        return tranMapper.queryPieVoStages();
    }
}
