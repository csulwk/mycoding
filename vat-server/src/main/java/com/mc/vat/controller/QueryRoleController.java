package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermTree;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.service.IPermissionInfoService;
import com.mc.vat.service.IRoleInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色信息管理
 * @author kai
 * @date 2020-04-15 21:45
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class QueryRoleController {

    private IRoleInfoService roleInfoService;
    private IPermissionInfoService permissionInfoService;
    @Autowired
    public QueryRoleController(IRoleInfoService roleInfoService, IPermissionInfoService permissionInfoService) {
        this.roleInfoService = roleInfoService;
        this.permissionInfoService = permissionInfoService;
    }

    @RequestMapping(value = "/perms/{id}", method = RequestMethod.GET)
    public JSONObject queryRolePermListByRoleId(@PathVariable(value = "id", required = true) Integer id) {
        log.info("根据角色ID查询角色权限信息 -> {}" , JSONObject.toJSONString(id));
        if (id == null) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        RoleInfo role = roleInfoService.getRoleInfoByRoleId(id);
        if (role == null) {
            return ResultUtil.resp(RetMsg.RET_E301);
        }
        List<PermTree> result = permissionInfoService.getRolePermListByRoleId(id);
        return ResultUtil.retSuccess(result);
    }

}
