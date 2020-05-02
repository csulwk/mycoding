package com.mc.vat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.Consts;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.UserRoleTable;
import com.mc.vat.entity.req.UserRoleReq;
import com.mc.vat.entity.resp.UserRoleResp;
import com.mc.vat.mapper.*;
import com.mc.vat.service.IUserInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    private final String OP_NAME = "coding";
    private final String SET_ENABLED = "1";

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
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addUserAndRole(UserRoleReq req) {
        UserInfo user = userInfoMapper.selectByUserName(req.getUsername());
        if (user != null) {
            return ResultUtil.resp(RetMsg.RET_E204);
        }
        user = new UserInfo();
        packageUser(user, req);
        user.setUiCreateBy(OP_NAME);
        userInfoMapper.saveUser(user);
        log.info("新增参数 -> {}；新增用户ID -> {}", req, user.getUiUserId());
        // 先新增用户信息再新增角色信息
        if (req.getRoleId() != null && user.getUiUserId() != null) {
            log.info("用户{}新增角色：{}", user.getUiUsername(), req.getRoleId());
            UserRoleTable userRoleTable = new UserRoleTable();
            userRoleTable.setUrtUserId(user.getUiUserId());
            userRoleTable.setUrtRoleId(req.getRoleId());
            userRoleTable.setUrtEnabled(SET_ENABLED);
            userRoleTable.setUrtCreateBy(OP_NAME);
            userRoleTableMapper.saveUserRole(userRoleTable);
        }
        return ResultUtil.retSuccess().fluentPut("userId", user.getUiUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateUser(UserRoleReq req) {
        UserInfo user = userInfoMapper.selectByUserName(req.getUsername());
        if (user == null) {
            return ResultUtil.resp(RetMsg.RET_E205);
        }
        UserRoleTable userRoleTable = userRoleTableMapper.selectUserRoleByUserId(user.getUiUserId());
        if (req.getRoleId() != null) {
            if (userRoleTable == null) {
                log.info("用户{}新增角色：{}", user.getUiUsername(), req.getRoleId());
                userRoleTable = new UserRoleTable();
                userRoleTable.setUrtUserId(user.getUiUserId());
                userRoleTable.setUrtRoleId(req.getRoleId());
                userRoleTable.setUrtEnabled(SET_ENABLED);
                userRoleTable.setUrtCreateBy(OP_NAME);
                userRoleTableMapper.saveUserRole(userRoleTable);
            } else if (!req.getRoleId().equals(userRoleTable.getUrtRoleId())){
                log.info("用户{}更新角色：{}->{}", user.getUiUsername(), userRoleTable.getUrtRoleId(), req.getRoleId());
                userRoleTable.setUrtRoleId(req.getRoleId());
                userRoleTable.setUrtUpdateBy(OP_NAME);
                userRoleTableMapper.updateUserRole(userRoleTable);
            } else {
                log.info("用户变更角色：{}->{}", req.getRoleId(), userRoleTable.getUrtRoleId());
            }
        }
        packageUser(user, req);
        user.setUiUpdateBy(OP_NAME);
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
        return userInfoMapper.getAllUserAllRole();
    }

    @Override
    public List<UserInfo> getUsersOfRoleByRoleId(Integer roleId) {
        return userInfoMapper.selectByRoleId(roleId);
    }

    /**
     * 封装用户信息
     * @param src 旧用户信息
     * @param des 新用户信息
     */
    private void packageUser(UserInfo src, UserRoleReq des) {
        src.setUiUsername(des.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String cipher = new SimpleHash(Consts.ENC_ALGORITHM, des.getPassword(), salt, Consts.ENC_TIMES).toString();
        log.info("原始密码是 {}, 盐是： {}, 加密密文是：{}", des.getPassword(), salt, cipher);
        src.setUiSalt(salt);
        src.setUiPassword(cipher);
        src.setUiUserDesc(des.getDesc());
        src.setUiSex(des.getSex() == null ?  0: des.getSex());
        src.setUiMobile(des.getMobile());
        src.setUiEmail(des.getEmail());
        src.setUiAvatar(des.getAvatar());
        src.setUiStatus(des.getStatus());
    }
}
