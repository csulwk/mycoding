package com.mc.mycodingserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求测试
 * @author kai
 * @date 2020-03-22 18:49
 */
@RestController
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "SUCCESS";
    }
}
