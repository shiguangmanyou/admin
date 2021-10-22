package com.bjpowernode.crm.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Enumeration;

/**
 * 负责项目中所有视图的跳转的Controller
 */
@Controller
public class ViewController {

    //测试Restful风格
    @RequestMapping("/login/{abc}/{def}")
    public void login(@PathVariable("abc") String username, @PathVariable("def") String password){
        System.out.println(username + ":" + password);
    }

    //return "workbench/activity/index"
    @RequestMapping(value = {"/toView/{firstView}/{secondView}","/toView/{firstView}/{secondView}/{thirdView}"
    ,"/toView/{firstView}/{secondView}/{thirdView}/{forthView}"})
    public String toView(@PathVariable(value = "firstView",required = false) String firstView,
                         @PathVariable(value = "secondView",required = false) String secondView,
                         @PathVariable(value = "thirdView",required = false)String thirdView,
                         @PathVariable(value = "forthView",required = false)String forthView,HttpServletRequest request){

        Enumeration<String> parameterNames = request.getParameterNames();
       /* while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            request.setAttribute(name, value);
        }*/
       for(;parameterNames.hasMoreElements();){
           String name = parameterNames.nextElement();
           String value = request.getParameter(name);
           request.setAttribute(name, value);
       }

        if( forthView != null){
            return firstView + File.separator + secondView + File.separator + thirdView + File.separator + forthView;
        }

        if( thirdView != null){
            return firstView + File.separator + secondView + File.separator + thirdView;
        }
        return firstView + File.separator + secondView;
    }
}
