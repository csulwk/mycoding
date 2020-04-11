package com.mc.vat.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.service.IPermissionInfoService;
import com.mc.vat.service.IRoleInfoService;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kai
 * @date 2020-04-06 15:55
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class QueryUserController {

    private IUserInfoService userInfoService;

    @Autowired
    public QueryUserController(IUserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public JSONObject queryUserByUsername(@PathVariable(value = "username", required = true) String username) {
        log.info("根据用户名称查询用户详细信息 -> {}" , JSONObject.toJSONString(username));
        if (StringUtils.isEmpty(username)) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        UserInfo result = userInfoService.getUserInfoByUsername(username);
        return ResultUtil.retSuccess(result);
    }

    @RequestMapping(value = "/role/{username}", method = RequestMethod.GET)
    public JSONObject queryRoleByUsername(@PathVariable(value = "username", required = true) String username) {
        log.info("根据用户名称查询用户角色信息 -> {}" , JSONObject.toJSONString(username));
        if (StringUtils.isEmpty(username)) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        List<String> result = userInfoService.getRoleInfoByUsername(username)
                .stream().map(role -> role.getRiRoleCode()).collect(Collectors.toList());
        return ResultUtil.retSuccess(result);
    }

    @RequestMapping(value = "/perm/{username}", method = RequestMethod.GET)
    public JSONObject queryPermissionByUsername(@PathVariable(value = "username", required = true) String username) {
        log.info("根据用户名称查询用户权限信息 -> {}" , JSONObject.toJSONString(username));
        if (StringUtils.isEmpty(username)) {
            return ResultUtil.resp(RetMsg.RET_E102);
        }
        List<String> result = userInfoService.getPermissionInfoByUsername(username)
                .stream().map(perm -> perm.getPiPermCode()).collect(Collectors.toList());
        return ResultUtil.retSuccess(result);
    }
}
