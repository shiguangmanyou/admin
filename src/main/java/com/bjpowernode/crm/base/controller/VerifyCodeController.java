package com.bjpowernode.crm.base.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class VerifyCodeController {

    @RequestMapping("/code")
    public void code(HttpServletResponse response, HttpSession session) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 10);
        String code = captcha.getCode();
        session.setAttribute("correctCode", code);
        //图形验证码写出，可以写出到文件，也可以写出到流
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
    }
}
