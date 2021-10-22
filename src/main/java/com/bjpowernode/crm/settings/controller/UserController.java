package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.exception.CrmEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.CommonUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class UserController {


    @Autowired
    private UserSerivce userSerivce;

    @RequestMapping("/settings/user/login")
    @ResponseBody
    public ResultVo login(User user, String code, HttpSession session, HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        try{
            //获取正确的验证码
            String correctCode = (String) session.getAttribute("correctCode");

            //获取用户登录的ip 127.0.0.1 0:0:0:0:0:0:1
            String remoteIp = request.getRemoteAddr();
            user.setAllowIps(remoteIp);
            user = userSerivce.login(user,code,correctCode);
            resultVo.setOk(true);
            //把用户存储到Session中
            session.setAttribute("user", user);
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //跳转到后台首页
    @RequestMapping("/settings/user/toIndex")
    public String toIndex(){
        return "workbench/index";
    }

    //退出系统
    @RequestMapping("/settings/user/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login.jsp";
    }

    //用户上传头像
    @RequestMapping("/settings/user/fileUpload")
    @ResponseBody
    public ResultVo fileUpload(HttpServletRequest request,HttpSession session,MultipartFile img){
        ResultVo resultVo = new ResultVo();
        try{
            String realPath = session.getServletContext().getRealPath("/upload");

            File file = new File(realPath);
            if(!file.exists()){
                //目录不存在
                file.mkdirs();//多级目录
            }

            //获取上传文件的名字 美女.webp
            String fileName = img.getOriginalFilename();
            fileName = UUIDUtil.getUUID() + fileName;

            //校验文件后缀名是否合法
            suffix(fileName);

            //校验文件大小是否合法
            maxSize(img.getSize());

            //.../upload/4324324美女.webp
            img.transferTo(new File(realPath + File.separator + fileName));

            //contextPath:/crm/upload/4324324美女.webp
            String contextPath = request.getContextPath();
            String photoPath = contextPath + File.separator + "upload" + File.separator + fileName;
            resultVo.setMessage("上传头像成功");
            resultVo.setT(photoPath);
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultVo;
    }

    //校验文件大小是否合法
    private void maxSize(long size) {
        long maxSize = 2 * 1024 * 1024;
        if(size > maxSize){
            throw new CrmException(CrmEnum.USER_UPDATE_SIZE);
        }
    }

    //校验文件后缀名是否合法 美女.webp
    private void suffix(String fileName) {
        //jpg,png,webp,gif
        String suffixs = "jpg,png,webp,gif";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(!suffixs.contains(suffix)){
            throw new CrmException(CrmEnum.USER_UPDATE_SUFFIX);
        }
    }

    //异步校验原始密码输入是否正确
    @RequestMapping("/settings/user/verifyOldPwd")
    @ResponseBody
    public ResultVo verifyOldPwd(String oldPwd,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            //获取当前登录用户的密码
            User user = (User) session.getAttribute("user");
            String correctPwd = user.getLoginPwd();
            userSerivce.verifyOldPwd(oldPwd,correctPwd);
            resultVo.setOk(true);
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }

    //异步更新用户信息
    @RequestMapping("/settings/user/updateUser")
    @ResponseBody
    public ResultVo updateUser(User user,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user1 = CommonUtil.getCurrentUser(session);
            user.setId(user1.getId());
            userSerivce.updateUser(user);
            resultVo.setOk(true);
            resultVo.setMessage("更新用户信息成功");
        }catch (CrmException e){
            resultVo.setMessage(e.getMessage());
        }
        return resultVo;
    }
}
