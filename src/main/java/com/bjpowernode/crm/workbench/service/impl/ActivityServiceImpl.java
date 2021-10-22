package com.bjpowernode.crm.workbench.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Activity> list(int page,int pageSize,Activity activity) {
        Example example = new Example(Activity.class);
        String name = activity.getName();
        Example.Criteria criteria = example.createCriteria();
        //判断名称
        if(StrUtil.isNotEmpty(name)){
            //条件查询
            criteria.andLike("name", "%" + name + "%");
        }

        String startDate = activity.getStartDate();
        //判断开始时间
        if(StrUtil.isNotEmpty(startDate)){
            //条件查询
            criteria.andGreaterThanOrEqualTo("startDate", startDate);
        }

        //判断结束时间
        if(StrUtil.isNotEmpty(activity.getEndDate())){
            //条件查询
            criteria.andLessThan("endDate", activity.getEndDate());
        }

        //判断所有者
        /**
         * 1、根据用户输入所有者查询用户表
         * 2、把查询出来的用户的主键放入到一个集合中，查询市场活动表，比较
         * 市场活动表中owner的值哪些是包含在用户主键集合中的那些数据
         */

        if(StrUtil.isNotEmpty(activity.getOwner())){
            //条件查询
            //1、根据用户输入所有者查询用户表
            Example example1 = new Example(User.class);
            example1.createCriteria().andLike("name", "%" + activity.getOwner() + "%");
            List<User> users = userMapper.selectByExample(example1);
            //2、把查询出来的用户的主键放入到一个集合中，查询市场活动表，比较
            // 市场活动表中owner的值哪些是包含在用户主键集合中的那些数据
            //定义一个集合，存储用户的主键
            List<String> ids = new ArrayList<>();
            for(User user : users){
                ids.add(user.getId());
            }
            criteria.andIn("id", ids);
        }
        //参数1:就是当前页码 page 参数2:每页记录数 pageSize
        //改行代码等同于 limit a,b
        PageHelper.startPage(page, pageSize);
        List<Activity> activities = activityMapper.selectByExample(example);
        for (Activity activity1 : activities) {
            //获取用户的外键
            String owner = activity1.getOwner();

            //查询用户
            User user = userMapper.selectByPrimaryKey(owner);

            activity1.setOwner(user.getName());
        }
        return activities;
    }

    @Override
    public List<User> queryUsers() {
        return userMapper.selectAll();
    }

    @Override
    public ResultVo saveOrUpdate(Activity activity,User user) {
        ResultVo resultVo = new ResultVo();
        if(activity.getId() == null){
            //添加操作
            activity.setId(UUIDUtil.getUUID());
            activity.setCreateBy(user.getName());
            activity.setCreateTime(DateTimeUtil.getSysTime());
            int count = activityMapper.insertSelective(activity);
            resultVo.setMessage("添加市场活动成功");
            if(count == 0){
                throw new CrmException(CrmEnum.ACTIVITY_ADD);
            }
        }else{
            //修改
            activity.setEditBy(user.getName());
            activity.setEditTime(DateTimeUtil.getSysTime());
            int count = activityMapper.updateByPrimaryKeySelective(activity);
            resultVo.setMessage("修改市场活动成功");
            if(count == 0){
                throw new CrmException(CrmEnum.ACTIVITY_UPDATE);
            }
        }
        resultVo.setOk(true);
       return resultVo;
    }

    @Override
    public Activity queryById(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteBatch(String ids) {

        //方式一:使用映射文件删除
        activityMapper.deleteActivities(ids.split(","));

      /*  if(count == 0){
            throw new CrmException(CrmEnum.ACTIVITY_DELETE);
        }*/
        //方式二:使用tkMapper删除
        //数组转换成List集合
      /*  List<String> aids = Arrays.asList(ids.split(","));

        //delete from 表 where id in(1,2,3)
        Example example = new Example(Activity.class);
        example.createCriteria().andIn("id",aids);

       int count =  activityMapper.deleteByExample(example);
       if(count == 0){
           throw new CrmException(CrmEnum.ACTIVITY_DELETE);
       }*/
    }

    @Override
    public Activity toDetail(String id) {
        //市场活动自己的数据
        Activity activity = activityMapper.selectByPrimaryKey(id);
        //市场活动备注数据
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setActivityId(activity.getId());
        List<ActivityRemark> activityRemarks = activityRemarkMapper.select(activityRemark);

        for (ActivityRemark remark : activityRemarks) {
            User user = userMapper.selectByPrimaryKey(remark.getOwner());
            remark.setImg(user.getImg());
        }

        //把备注集合设置到市场活动中
        activity.setActivityRemarks(activityRemarks);
        return activity;
    }

    @Override
    public void saveRemark(ActivityRemark activityRemark,User user) {
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateBy(user.getName());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");
        activityRemark.setImg(user.getImg());
        activityRemark.setOwner(user.getId());
        activityRemarkMapper.insertSelective(activityRemark);
    }

    @Override
    public void updateRemark(ActivityRemark activityRemark, User user) {
        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(user.getName());
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemarkMapper.updateByPrimaryKeySelective(activityRemark);
    }

    @Override
    public void deleteRemark(String id) {
        activityRemarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ExcelWriter exportExcel() {
        Example example = new Example(Activity.class);
        Map<String, EntityColumn> propertyMap = example.getPropertyMap();
        List<Activity> activities = activityMapper.selectByExample(example);

        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 定义单元格背景色
        StyleSet style = writer.getStyleSet();
        // 第二个参数表示是否也设置头部单元格背景
        style.setBackgroundColor(IndexedColors.PINK1, false);
        writer.merge(propertyMap.size() - 1, "市场活动统计数据");
        writer.write(activities, true);
        return writer;
    }

}
