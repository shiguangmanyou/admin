package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;


    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactActivityRelationMapper contactActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public Clue toDetail(String id) {
        //1、线索自己的信息
        Clue clue = clueMapper.selectByPrimaryKey(id);

        //2、线索备注信息 省略

        //3、关联的市场活动信息
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);

        //定义一个集合存储中间表的市场活动主键
        List<String> aids = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            aids.add(activityRelation.getActivityId());
        }

        Example example = new Example(Activity.class);
        example.createCriteria().andIn("id", aids);
        List<Activity> activities = activityMapper.selectByExample(example);

        //把集合设置到线索对象中
        clue.setActivities(activities);
        return clue;
    }

    @Override
    public List<Activity> queryActivities(String id,String name) {
        //1、查询出线索已经关联的市场活动
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);

        //定义一个集合存储中间表的市场活动主键
        List<String> aids = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            aids.add(activityRelation.getActivityId());
        }

        //2、根据输入的市场活动名称模糊查询满足条件的市场活动，排除已经关联的市场活动 where id in()
        Example example = new Example(Activity.class);
        Example.Criteria criteria = example.createCriteria();
        if(aids.size() != 0){
            criteria.andNotIn("id", aids);
        }
        criteria.andLike("name", "%" + name + "%");
        List<Activity> activities = activityMapper.selectByExample(example);
        return activities;
    }

    @Override
    public ResultVo bind(String ids, String id) {
        ResultVo resultVo = new ResultVo();
        //分割字符串
        String[] aids = ids.split(",");
        //定义一个，存储用于返回给前台刚添加的市场活动
        List<Activity> activities = new ArrayList<>();
        for (String aid : aids) {
            //根据市场活动主键查询市场活动
            Activity activity = activityMapper.selectByPrimaryKey(aid);

            activities.add(activity);
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(id);
            clueActivityRelation.setActivityId(aid);
            clueActivityRelationMapper.insertSelective(clueActivityRelation);
        }
        resultVo.setT(activities);
        resultVo.setOk(true);
        resultVo.setMessage("绑定成功");
        return resultVo;
    }

    @Override
    public void unbind(ClueActivityRelation clueActivityRelation) {
        clueActivityRelationMapper.delete(clueActivityRelation);
    }

    @Override
    public Clue queryById(String id) {
        return clueMapper.selectByPrimaryKey(id);
    }

    @Override
    public void convert(String id,String isCreateTransaction, Tran tran,User user) {
        int count = 0;
        //1、根据线索的主键查询线索的信息(线索包含自身的信息，包含客户的信息，包含联系人信息)
        Clue clue = clueMapper.selectByPrimaryKey(id);

        //2、先将线索中的客户信息取出来保存在客户表中，当该客户不存在的时候，新建客户(按公司名称精准查询)
        //查询客户表中是否有指定客户
        Customer customer = new Customer();


        customer.setName(clue.getCompany());
        List<Customer> customers = customerMapper.select(customer);

        if(customers.size() == 0){
            //客户不存在，线索中的客户信息取出来保存在客户表中
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(user.getName());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            count = customerMapper.insertSelective(customer);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_CONVERT);
            }
        }
        //3、将线索中联系人信息取出来保存在联系人表中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getName());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setAddress(clue.getAddress());
        count = contactsMapper.insertSelective(contacts);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }

        //4、线索中的备注信息取出来保存在客户备注和联系人备注中
        //保存联系人备注
        ContactsRemark contactsRemark = new ContactsRemark();
        contactsRemark.setId(UUIDUtil.getUUID());
        contactsRemark.setCreateBy(user.getName());
        contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
        contactsRemark.setContactsId(contacts.getId());

        count = contactsRemarkMapper.insertSelective(contactsRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }

        //保存客户备注
        CustomerRemark customerRemark = new CustomerRemark();
        customerRemark.setId(UUIDUtil.getUUID());
        customerRemark.setCreateBy(user.getName());
        customerRemark.setCreateTime(DateTimeUtil.getSysTime());
        customerRemark.setCustomerId(customer.getId());
        count = customerRemarkMapper.insertSelective(customerRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }

        //5、将"线索和市场活动的关系"转换到"联系人和市场活动的关系中"
        //5.1 查询线索关联的市场活动
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);

        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);

        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            ContactActivityRelation contactActivityRelation = new ContactActivityRelation();
            contactActivityRelation.setId(UUIDUtil.getUUID());
            contactActivityRelation.setContactsId(contacts.getId());
            contactActivityRelation.setActivityId(activityRelation.getActivityId());

            count =  contactActivityRelationMapper.insert(contactActivityRelation);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_CONVERT);
            }
        }

        //6、如果转换过程中发生了交易，创建一条新的交易，交易信息不全，后面可以通过修改交易来完善交易信息
        if("1".equals(isCreateTransaction)){
            //发生交易了,保存交易数据
            tran.setId(UUIDUtil.getUUID());
            count = tranMapper.insertSelective(tran);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_CONVERT);
            }
            //7、创建该条交易对应的交易历史以及备注
            //保存交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());

            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setCreateBy(user.getName());
            tranHistory.setTranId(tran.getId());

            count = tranHistoryMapper.insert(tranHistory);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_CONVERT);
            }

            //保存交易备注
            TranRemark tranRemark = new TranRemark();
            tranRemark.setId(UUIDUtil.getUUID());
            tranRemark.setCreateBy(user.getName());
            tranRemark.setCreateTime(DateTimeUtil.getSysTime());
            tranRemark.setTranId(tran.getId());

            count = tranRemarkMapper.insert(tranRemark);
            if(count == 0){
                throw new CrmException(CrmEnum.CLUE_CONVERT);
            }
        }
        // 8、删除线索的备注信息
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setClueId(id);
        count = clueRemarkMapper.delete(clueRemark);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }

        //9、删除线索和市场活动的关联关系
        ClueActivityRelation clueActivityRelation1 = new ClueActivityRelation();
        clueActivityRelation1.setClueId(id);
        count = clueActivityRelationMapper.delete(clueActivityRelation1);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }

        //10、删除线索
        count = clueMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new CrmException(CrmEnum.CLUE_CONVERT);
        }
    }

    @Override
    public List<Activity> searchActivity(String id,String name) {
        //查询当前线索已经关联的市场活动
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(id);
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);

        //定义一个集合，存储市场活动主键
        List<String> ids = new ArrayList<>();

        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            ids.add(activityRelation.getActivityId());
        }

        //查询满足条件的市场活动
        Example example = new Example(Activity.class);
        example.createCriteria().andIn("id", ids).andLike("name", "%" + name + "%");
        return  activityMapper.selectByExample(example);
    }
}
