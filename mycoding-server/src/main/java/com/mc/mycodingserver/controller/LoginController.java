package com.mc.mycodingserver.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆请求
 * @author kai
 * @date 2020-03-22 18:33
 */
@Slf4j
@RestController
@RequestMapping(value = "/mc")
public class LoginController {
    @GetMapping("/login")
    public JSONObject login() {
        log.info("请求登陆...");
        return new JSONObject().fluentPut("RESULT","SUCCESS");
    }
}
