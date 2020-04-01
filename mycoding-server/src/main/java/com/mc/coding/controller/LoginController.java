package com.mc.coding.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.coding.constant.RetMsg;
import com.mc.coding.entity.req.User;
import com.mc.coding.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

/**
 * 登录请求
 * @author kai
 * @date 2020-03-22 18:33
 */
@Slf4j
@RestController
@RequestMapping(value = "/mc/user")
public class LoginController {

    @PostMapping("/login")
    public JSONObject login(@RequestBody User user) {
        log.info("请求登录, {}", user);
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = HtmlUtils.htmlEscape(user.getUsername());
        String password = user.getPassword();

        if (!"tom".equals(username) || !"123".equals(password)) {
            log.info("登录认证失败: username={}, password={}", username, password);
            return ResultUtil.resp(RetMsg.RET_E201);
        } else {
            return ResultUtil.retSuccess("ok");
        }
    }

    @GetMapping("/info")
    public JSONObject getInfo() {
        return ResultUtil.retSuccess("获取用户信息成功");
    }
}
