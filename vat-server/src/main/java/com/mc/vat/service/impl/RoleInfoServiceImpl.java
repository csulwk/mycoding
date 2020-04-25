package com.mc.vat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.RolePermissionTable;
import com.mc.vat.entity.req.RolePermReq;
import com.mc.vat.mapper.PermissionInfoMapper;
import com.mc.vat.mapper.RoleInfoMapper;
import com.mc.vat.mapper.RolePermissionTableMapper;
import com.mc.vat.service.IRoleInfoService;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色处理
 * @author kai
 * @date 2020-04-04 16:43
 */
@Service("roleInfoService")
@Slf4j
public class RoleInfoServiceImpl implements IRoleInfoService {

    private RoleInfoMapper roleInfoMapper;
    private PermissionInfoMapper permissionInfoMapper;
    private RolePermissionTableMapper rolePermissionTableMapper;
    private final String OP_NAME = "coding";
    private final String SET_ENABLED = "1";

    @Autowired
    public RoleInfoServiceImpl(RoleInfoMapper roleInfoMapper, PermissionInfoMapper permissionInfoMapper,
                               RolePermissionTableMapper rolePermissionTableMapper) {
        this.roleInfoMapper = roleInfoMapper;
        this.permissionInfoMapper = permissionInfoMapper;
        this.rolePermissionTableMapper = rolePermissionTableMapper;
    }

    @Override
    public List<RoleInfo> getAllRoleInfo() {
        List<RoleInfo> results = roleInfoMapper.selectAllRoleInfo();
        log.info("无参查询结果 -> {}", results);
        return results;
    }

    @Override
    public List<RoleInfo> getRoleListByUserIds(List<Integer> userIds) {
        List<RoleInfo> results = roleInfoMapper.selectByRoleIds(userIds);
        log.info("查询参数 -> {}；查询结果 -> {}", userIds, results);
        return results;
    }

    @Override
    public RoleInfo getRoleInfoByRoleId(Integer roleId) {
        RoleInfo result = roleInfoMapper.selectByRoleId(roleId);
        log.info("查询参数 -> {}；查询结果 -> {}", roleId, result);
        return result;
    }

    @Override
    public RoleInfo getRoleInfoByRoleCode(String roleCode) {
        RoleInfo result = roleInfoMapper.selectByRoleCode(roleCode);
        log.info("查询参数 -> {}；查询结果 -> {}", roleCode, result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addRoleAndPerm(RolePermReq req) {
        // 查询角色是否存在
        RoleInfo roleInfo = roleInfoMapper.selectByRoleCode(req.getRoleCode());
        if (roleInfo != null) {
            log.warn("该角色已存在 -> {}", req);
            return ResultUtil.resp(RetMsg.RET_E302);
        }
        // 角色不存在则添加
        roleInfo = packageRole(req);
        roleInfo.setRiStatus(SET_ENABLED);
        roleInfo.setRiCreateBy(OP_NAME);
        roleInfo.setRiUpdateBy(OP_NAME);
        roleInfoMapper.saveRole(roleInfo);
        log.info("添加的角色ID -> {}", roleInfo.getRiRoleId());
        // 角色赋权处理
        if (req.getPermList().size() > 0) {
            for (Integer permId : req.getPermList()) {
                // 查询权限是否存在
                PermissionInfo permInfo = permissionInfoMapper.selectByPermId(permId);
                if (permInfo == null) {
                    log.warn("该权限不存在 -> {}", permId);
                    return ResultUtil.resp(RetMsg.RET_E309);
                }
                // 查询角色权限是否存在
                RolePermissionTable rolePerm = rolePermissionTableMapper.selectByRoleIdAndPermId(roleInfo.getRiRoleId(),
                        permInfo.getPiPermId());
                if (rolePerm == null) {
                    // 角色权限不存在则添加
                    rolePerm = packageRolePerm(roleInfo.getRiRoleId(), permInfo.getPiPermId());
                    rolePermissionTableMapper.saveRolePerm(rolePerm);
                }
                log.info("添加的权限ID -> {}", rolePerm.getRptId());

            }
        }
        return ResultUtil.retSuccess().fluentPut("roleId", roleInfo.getRiRoleId());
    }

    /**
     * 封装角色信息
     * @param des 输入信息
     * @return 角色信息
     */
    private RoleInfo packageRole(RolePermReq des) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRiRoleCode(des.getRoleCode());
        roleInfo.setRiRoleDesc(des.getRoleDesc());
        roleInfo.setRiStatus(des.getRoleStat());
        return roleInfo;
    }

    /**
     * 封装权限信息
     * @param roleId 角色ID
     * @param permId 权限ID
     * @return 角色权限信息
     */
    private RolePermissionTable packageRolePerm(Integer roleId, Integer permId) {
        RolePermissionTable rolePerm = new RolePermissionTable();
        rolePerm.setRptRoleId(roleId);
        rolePerm.setRptPermId(permId);
        return rolePerm;
    }

}
