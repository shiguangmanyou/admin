package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.base.bean.DicType;
import com.bjpowernode.crm.base.bean.DicValue;
import com.bjpowernode.crm.base.mapper.DicTypeMapper;
import com.bjpowernode.crm.base.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.*;

/*
* Controller
* Service
* Repository
* Component
* request,session,servletContext(application),pageContext
* */
@Component
public class CrmCache {

    @Autowired
    private DicTypeMapper dicTypeMapper;

    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private UserMapper userMapper;

    //等同于配置文件中<bean init-method>
    @PostConstruct
    public void init(){

        //缓冲所有者信息
        List<User> users = userMapper.selectAll();

        servletContext.setAttribute("users", users);

        //先查询字典类型
        List<DicType> dicTypes = dicTypeMapper.selectAll();

        //定义一个Map<String,List<DicValue>>存储字典数据
        Map<String,List<DicValue>> map = new HashMap<>();
        for (DicType dicType : dicTypes) {
            //获取类型主键，就是value中的外键
            String code = dicType.getCode();
            //根据外键查询满足的字典值(多个)
            Example example = new Example(DicValue.class);
            example.setOrderByClause("orderNo");
            example.createCriteria().andEqualTo("typeCode", code);
            List<DicValue> dicValues = dicValueMapper.selectByExample(example);

            map.put(code, dicValues);
        }

        //把数据字典数据放入到ServletContext中
        servletContext.setAttribute("map", map);
        
        
        //缓冲阶段和可能性数据
        Map<String,String> stage2PossibilityMap = new TreeMap<>();
        //该方法默认只读取properties文件 报名中间使用.分割
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Mybatis.Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();

      while(keys.hasMoreElements()){
          String key = keys.nextElement();
          String value = resourceBundle.getString(key);
          stage2PossibilityMap.put(key, value);
      }

        System.out.println(stage2PossibilityMap);

      //把阶段和可能性的数据缓冲
        servletContext.setAttribute("stage2PossibilityMap", stage2PossibilityMap);
    }
}
