package com.mc.vat.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求测试
 * @author kai
 * @date 2020-03-22 18:49
 */
@RestController
@RequestMapping(value = "/mc")
public class IndexController {

    /**
     * RequiresRoles：用户角色验证
     * RequiresPermissions：用户权限验证
     * MyExceptionHandler.unauthorizedHandler()：捕获并处理异常信息
     * @return
     */
    @RequiresRoles("ADMIN")
    @RequiresPermissions("XTJK")
    @GetMapping("/index")
    public String index() {
        return "SUCCESS";
    }
}
