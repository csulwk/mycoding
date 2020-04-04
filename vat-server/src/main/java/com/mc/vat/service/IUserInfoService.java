package com.mc.vat.service;

import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;

import java.util.List;

/**
 * 用户操作
 * @author kai
 * @date 2020-04-04 15:24
 */
public interface IUserInfoService {

    /**
     * 根据用户登录名称查询用户信息
     * @param username 用户登录名称
     * @return 用户信息
     */
    UserInfo getUserInfoByUsername(String username);

    /**
     * 获取所有的用户信息
     * @return 用户信息
     */
    List<UserInfo> getAllUserInfo();

    /**
     * 根据用户登录名称查询角色信息
     * @param username 用户登录名称
     * @return 角色信息
     */
    List<RoleInfo> getRoleInfoByUsername(String username);

    /**
     * 根据用户登录名称查询权限信息
     * @param username 用户登录名称
     * @return 权限信息
     */
    List<PermissionInfo> getPermissionInfoByUsername(String username);

}