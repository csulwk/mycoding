package com.mc.vat.service;

import com.mc.vat.entity.PermissionInfo;

import java.util.List;

/**
 * 权限操作
 * @author kai
 * @date 2020-04-04 15:44
 */
public interface IPermissionInfoService {

    /**
     * 获取所有的权限信息
     * @return 权限信息
     */
    List<PermissionInfo> getAllPermissionInfo();

    /**
     * 根据角色ID列表查询权限信息
     * @param roleIds 角色ID
     * @return 权限信息
     */
    List<PermissionInfo> getPermissionListByRoleIds(List<Integer> roleIds);
}
