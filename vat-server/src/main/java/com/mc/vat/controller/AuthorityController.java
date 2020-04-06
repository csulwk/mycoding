package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.service.IPermissionInfoService;
import com.mc.vat.service.IRoleInfoService;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kai
 * @date 2020-04-06 15:55
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthorityController {

    private IUserInfoService userInfoService;
    private IRoleInfoService roleInfoService;
    private IPermissionInfoService permissionInfoService;

    @Autowired
    public AuthorityController(IUserInfoService userInfoService, IRoleInfoService roleInfoService,
                               IPermissionInfoService permissionInfoService) {
        this.userInfoService = userInfoService;
        this.roleInfoService = roleInfoService;
        this.permissionInfoService = permissionInfoService;
    }


    @GetMapping("/user/info")
    public JSONObject getAllUsers() {
        List<UserInfo> result = userInfoService.getAllUserInfo();
        return ResultUtil.retSuccess(result);
    }

    @GetMapping("/role/info")
    public JSONObject getAllRoles() {
        List<RoleInfo> result = roleInfoService.getAllRoleInfo();
        return ResultUtil.retSuccess(result);
    }

    @GetMapping("/perm/info")
    public JSONObject getAllPerms() {
        List<PermissionInfo> result = permissionInfoService.getAllPermissionInfo();
        return ResultUtil.retSuccess(result);
    }

}
