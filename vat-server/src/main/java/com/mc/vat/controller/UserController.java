package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.req.User;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kai
 * @date 2020-04-01 0:04
 */
@Slf4j
@RestController
@RequestMapping(value = "/mc/user")
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

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
     * 用户登出
     * @return
     */
    @RequestMapping("/logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        log.info("获取当前用户信息 -> {}", JSONObject.toJSON(subject.getPrincipal()));
        subject.logout();
        return ResultUtil.retSuccess("当前用户已退出登录");
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    @GetMapping("/info")
    public JSONObject getLoginInfo() {
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        log.info("获取当前用户信息 -> {}", user);
        JSONObject obj = new JSONObject();
        obj.put("name", user.getUiUsername());
        obj.put("email", user.getUiEmail());
        obj.put("introduction", user.getUiUserDesc());
        obj.put("avatar", user.getUiAvatar());

        List<String> roleCodes = userInfoService.getRoleInfoByUsername(user.getUiUsername())
                .stream().map(role -> role.getRiRoleCode()).collect(Collectors.toList());
        log.info("当前用户的角色信息 -> {}", roleCodes);
        obj.put("roles", roleCodes);

        List<String> permCodes = userInfoService.getPermissionInfoByUsername(user.getUiUsername())
                .stream().map(perm -> perm.getPiPermCode()).collect(Collectors.toList());
        log.info("当前用户的权限信息 -> {}", permCodes);
        obj.put("perms", permCodes);
        return ResultUtil.retSuccess(obj);
    }

    /**
     * 用户未登录返回信息
     * @return
     */
    @RequestMapping(value = "/unlogged")
    public JSONObject unLogged() {
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        log.info("用户未登录 -> {}", user);
        return ResultUtil.resp(RetMsg.RET_E202);
    }

    /**
     * 用户权限不足返回信息
     * @return
     */
    @RequestMapping(value = "/unauthorized")
    public JSONObject unauthorized(){
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        log.info("用户权限不足 -> {}", user);
        return ResultUtil.resp(RetMsg.RET_E203);
    }

}
