package com.mc.vat.service;

import com.mc.vat.entity.RoleInfo;

import java.util.List;

/**
 * 角色操作
 * @author kai
 * @date 2020-04-04 15:44
 */
public interface IRoleInfoService {

    /**
     * 获取所有的角色信息
     * @return 角色信息
     */
    List<RoleInfo> getAllRoleInfo();

    /**
     * 根据用户ID列表查询角色信息
     * @param userIds 用户ID
     * @return 角色信息
     */
    List<RoleInfo> getRoleListByUserIds(List<Integer> userIds);
}
