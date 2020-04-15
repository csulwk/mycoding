package com.mc.vat.controller;

import com.mc.vat.service.IRoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色信息维护
 * @author kai
 * @date 2020-04-15 21:45
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class QueryRoleController {

    @Autowired
    private IRoleInfoService roleInfoService;
    public QueryRoleController(IRoleInfoService roleInfoService) {
        this.roleInfoService = roleInfoService;
    }

    
}
