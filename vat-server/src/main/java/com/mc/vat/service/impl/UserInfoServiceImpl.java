package com.mc.vat.service.impl;

import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.mapper.*;
import com.mc.vat.service.IUserInfoService;
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

}
