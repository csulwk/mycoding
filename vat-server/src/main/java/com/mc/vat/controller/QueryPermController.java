package com.mc.vat.controller;

import com.mc.vat.service.IPermissionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限信息维护
 * @author kai
 * @date 2020-04-20 23:07
 */
@RestController
@RequestMapping("/perm")
@Slf4j
public class QueryPermController {

    @Autowired
    private IPermissionInfoService permissionInfoService;

}
