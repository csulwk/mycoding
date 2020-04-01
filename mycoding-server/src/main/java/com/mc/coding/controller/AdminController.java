package com.mc.coding.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.coding.constant.RetMsg;
import com.mc.coding.entity.Admin;
import com.mc.coding.entity.req.User;
import com.mc.coding.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author kai
 * @date 2020-04-01 0:04
 */
@Slf4j
@RestController
@RequestMapping(value = "/mc/admin")
public class AdminController {

    /**
     * 用户登陆
     * @param user
     * @return
     */
    @PostMapping("/login")
    public JSONObject login(@RequestBody User user) {
        log.info("请求登录, {}", user);
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = HtmlUtils.htmlEscape(user.getUsername());
        String password = user.getPassword();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            return ResultUtil.resp(RetMsg.ERROR,"密码错误");
        } catch (UnknownAccountException uae) {
            return ResultUtil.resp(RetMsg.ERROR,"用户名错误");
        }
        Serializable sessionId = subject.getSession().getId();
        JSONObject obj = new JSONObject();
        obj.put("token", sessionId);
        return ResultUtil.retSuccess(obj);
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    @GetMapping("/info")
    public JSONObject getLoginInfo() {
        Admin admin = (Admin) getLoginUser();
        JSONObject obj = new JSONObject();
        obj.put("name", admin.getUsername());
        obj.put("email", admin.getEmail());
        obj.put("introduction", admin.getMotto());
        obj.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        obj.put("roles", Arrays.asList(new String[]{"admin", "delver"}));
        return ResultUtil.retSuccess(obj);
    }

    private Object getLoginUser() {
        Session session = SecurityUtils.getSubject().getSession();
        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (principalCollection == null) {
            return null;
        }
        return principalCollection.getPrimaryPrincipal();
    }
}
