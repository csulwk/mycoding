package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermTree;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.req.RolePermReq;
import com.mc.vat.service.IPermissionInfoService;
import com.mc.vat.service.IRoleInfoService;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private IUserInfoService userInfoService;
    private IRoleInfoService roleInfoService;
    private IPermissionInfoService permissionInfoService;
    @Autowired
    public QueryRoleController(IUserInfoService userInfoService, IRoleInfoService roleInfoService,
                               IPermissionInfoService permissionInfoService) {
        this.userInfoService = userInfoService;
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

    @RequestMapping(value = "/{roleId}/perms", method = RequestMethod.GET)
    public JSONObject queryPermIdsByRoleId(@PathVariable(value = "roleId", required = true) Integer roleId) {
        log.info("根据角色ID查询权限ID列表 -> {}" , JSONObject.toJSONString(roleId));
        if (roleId == null) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        List<Integer> result = roleInfoService.getPermIdsByRoleId(roleId);
        return ResultUtil.retSuccess(result);
    }

    @RequestMapping(value = "/{roleId}/users", method = RequestMethod.GET)
    public JSONObject queryUsersOfRoleByRoleId(@PathVariable(value = "roleId", required = true) Integer roleId) {
        log.info("根据角色ID查询归属的用户列表 -> {}" , JSONObject.toJSONString(roleId));
        if (roleId == null) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        RoleInfo role = roleInfoService.getRoleInfoByRoleId(roleId);
        if (role == null) {
            return ResultUtil.resp(RetMsg.RET_E301);
        }
        List<UserInfo> result = userInfoService.getUsersOfRoleByRoleId(roleId);
        return ResultUtil.retSuccess(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JSONObject addRoleAndPerm(@RequestBody RolePermReq req) {
        log.info("根据输入参数添加角色信息 -> {}" , JSONObject.toJSONString(req));
        // TODO 获取指定节点的父节点信息
        // https://blog.csdn.net/lan_qinger/article/details/84284194
        // https://blog.csdn.net/u013887008/article/details/81025779
        return roleInfoService.addRoleAndPerm(req);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public JSONObject updateRoleAndPerm(@RequestBody RolePermReq req) {
        log.info("根据输入参数更新角色信息 -> {}" , JSONObject.toJSONString(req));
        return roleInfoService.updateRoleAndPerm(req);
    }

    @RequestMapping(value = "/delete/{roleCode}", method = RequestMethod.DELETE)
    public JSONObject deleteRoleAndPerm(@PathVariable(value = "roleCode", required = true) String roleCode) {
        log.info("根据角色代码删除角色信息 -> {}" , JSONObject.toJSONString(roleCode));
        return roleInfoService.deleteRoleAndPerm(roleCode);
    }
}
