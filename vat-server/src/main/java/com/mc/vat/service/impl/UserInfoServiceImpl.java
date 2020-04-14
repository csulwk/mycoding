package com.mc.vat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.req.UserRoleReq;
import com.mc.vat.entity.resp.UserRoleResp;
import com.mc.vat.mapper.*;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户处理
 * @author kai
 * @date 2020-04-04 16:45
 */
@Service("userInfoService")
@Slf4j
public class UserInfoServiceImpl implements IUserInfoService {

    private UserInfoMapper userInfoMapper;
    private UserRoleTableMapper userRoleTableMapper;
    private RoleInfoMapper roleInfoMapper;
    private RolePermissionTableMapper rolePermissionTableMapper;
    private PermissionInfoMapper permissionInfoMapper;
    private final String NAME = "lwk";

    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper, UserRoleTableMapper userRoleTableMapper,
                               RoleInfoMapper roleInfoMapper, RolePermissionTableMapper rolePermissionTableMapper,
                               PermissionInfoMapper permissionInfoMapper ) {
        this.userInfoMapper = userInfoMapper;
        this.userRoleTableMapper = userRoleTableMapper;
        this.roleInfoMapper = roleInfoMapper;
        this.rolePermissionTableMapper = rolePermissionTableMapper;
        this.permissionInfoMapper = permissionInfoMapper;
    }

    @Override
    public UserInfo getUserInfoByUsername(String username) {
        return userInfoMapper.selectByUserName(username);
    }

    @Override
    public List<UserInfo> getAllUserInfo() {
        List<UserInfo> results = userInfoMapper.selectAllUserInfo();
        log.info("无参查询结果 -> {}", results);
        return results;
    }

    @Override
    public List<RoleInfo>  getRoleInfoByUsername(String username) {
        UserInfo user = userInfoMapper.selectByUserName(username);
        List<Integer> roleIds = userRoleTableMapper.selectRoleIdsByUserId(user.getUiUserId());
        List<RoleInfo> results = roleInfoMapper.selectByRoleIds(roleIds);
        log.info("查询参数 -> {}；查询结果 -> {}", username, results);
        return results;
    }

    @Override
    public List<PermissionInfo> getPermissionInfoByUsername(String username) {
        UserInfo user = userInfoMapper.selectByUserName(username);
        List<Integer> roleIds = userRoleTableMapper.selectRoleIdsByUserId(user.getUiUserId());
        List<Integer> permIds = rolePermissionTableMapper.selectPermIdsByRoleIds(roleIds);
        List<PermissionInfo> results = permissionInfoMapper.selectByPermIds(permIds);
        log.info("查询参数 -> {}；查询结果 -> {}", username, results);
        return results;
    }

    @Override
    public JSONObject addUserAndRole(UserRoleReq req) {
        UserInfo user = userInfoMapper.selectByUserName(req.getUsername());
        if (user != null) {
            return ResultUtil.resp(RetMsg.RET_E204);
        }
        packageUser(user, req);
        userInfoMapper.saveUser(user);
        log.info("新增参数 -> {}；新增用户ID -> {}", req, user.getUiUserId());
        return ResultUtil.retSuccess().fluentPut("userId", user.getUiUserId());
    }

    @Override
    public JSONObject updateUser(UserRoleReq req) {
        UserInfo user = userInfoMapper.selectByUserName(req.getUsername());
        if (user == null) {
            return ResultUtil.resp(RetMsg.RET_E205);
        }
        packageUser(user, req);
        userInfoMapper.updateUser(user);
        log.info("更新参数 -> {}；更新用户ID -> {}", req, user.getUiUserId());
        return ResultUtil.retSuccess().fluentPut("userId", user.getUiUserId());
    }

    @Override
    public JSONObject deleteByUsername(String username) {
        UserInfo user = userInfoMapper.selectByUserName(username);
        if (user == null) {
            return ResultUtil.resp(RetMsg.RET_E205);
        }
        userInfoMapper.deleteByUserId(user.getUiUserId());
        log.info("删除参数 -> {}；删除用户ID -> {}", username, user.getUiUserId());
        return ResultUtil.retSuccess().fluentPut("userId", user.getUiUserId());
    }

    @Override
    public List<UserRoleResp> getAllUserAllRole() {
        List<UserRoleResp> ur = userInfoMapper.getAllUserAllRole();
        return ur;
    }

    /**
     * 封装用户信息
     * @param src 旧用户信息
     * @param des 新用户信息
     */
    private void packageUser(UserInfo src, UserRoleReq des) {
        src.setUiUsername(des.getUsername());
        src.setUiPassword(des.getPassword());
        src.setUiUserDesc(des.getDesc());
        src.setUiSex(des.getSex() == null ?  0: des.getSex());
        src.setUiMobile(des.getMobile());
        src.setUiEmail(des.getEmail());
        src.setUiAvatar(des.getAvatar());
        src.setUiStatus(des.getStatus());
        src.setUiCreateBy(NAME);
        src.setUiUpdateBy(NAME);
    }
}
