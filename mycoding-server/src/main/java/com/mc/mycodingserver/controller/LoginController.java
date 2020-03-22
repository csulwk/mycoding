package com.mc.mycodingserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.mycodingserver.constant.RetMsg;
import com.mc.mycodingserver.entity.req.User;
import com.mc.mycodingserver.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

/**
 * 登陆请求
 * @author kai
 * @date 2020-03-22 18:33
 */
@Slf4j
@RestController
@RequestMapping(value = "/mc")
public class LoginController {

    @PostMapping("/login")
    public JSONObject login(@RequestBody User user) {
        log.info("请求登陆, {}", user);
        // 对 html 标签进行转义，防止 XSS 攻击
        String userName = HtmlUtils.htmlEscape(user.getUserName());
        String passWord = user.getPassWord();

        if (!"admin".equals(userName) || !"123456".equals(passWord)) {
            log.info("登录认证失败: userName={}, passWord={}", userName, passWord);
            return ResultUtil.resp(RetMsg.RET_E201);
        } else {
            return ResultUtil.retSuccess("ok");
        }
    }
}
