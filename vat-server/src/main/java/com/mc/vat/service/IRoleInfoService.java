package com.mc.vat.service;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.req.RolePermReq;

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

    /**
     * 根据角色ID查询角色信息
     * @param roleId 角色ID
     * @return 角色信息
     */
    RoleInfo getRoleInfoByRoleId(Integer roleId);

    /**
     * 根据角色代码查询角色信息
     * @param roleCode 角色代码
     * @return 角色信息
     */
    RoleInfo getRoleInfoByRoleCode(String roleCode);

    /**
     * 添加角色信息
     * @param req 角色信息
     * @return 角色信息
     */
    JSONObject addRoleAndPerm(RolePermReq req);

}
