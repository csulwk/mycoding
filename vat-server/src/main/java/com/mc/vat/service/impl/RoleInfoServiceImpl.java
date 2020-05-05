package com.mc.vat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.Const;
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
    public List<Integer> getPermIdsByRoleId(Integer roleId) {
        return rolePermissionTableMapper.selectPermIdsByRoleId(roleId, false);
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
        roleInfo = new RoleInfo();
        packageRole(roleInfo, req);
        roleInfo.setRiStatus(Const.USER_ENABLED_TRUE);
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
                    throw new RuntimeException("添加角色时权限异常...");
                }
                // 查询角色权限是否存在
                RolePermissionTable rolePerm = rolePermissionTableMapper.selectByRoleIdAndPermId(roleInfo.getRiRoleId(),
                        permInfo.getPiPermId());
                if (rolePerm == null) {
                    // 角色权限不存在则添加
                    rolePerm = packageRolePerm(roleInfo.getRiRoleId(), permInfo.getPiPermId());
                    rolePerm.setRptEnabled(Const.USER_ENABLED_TRUE);
                    rolePerm.setRptCreateBy(OP_NAME);
                    rolePerm.setRptUpdateBy(OP_NAME);
                    rolePermissionTableMapper.saveRolePerm(rolePerm);
                }
                log.info("添加的权限ID -> {}", rolePerm.getRptId());
            }
        }
        return ResultUtil.retSuccess().fluentPut("roleId", roleInfo.getRiRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateRoleAndPerm(RolePermReq req) {
        // 查询角色是否存在
        RoleInfo roleInfo = roleInfoMapper.selectByRoleCode(req.getRoleCode());
        if (roleInfo == null) {
            log.warn("该角色不存在 -> {}", req);
            return ResultUtil.resp(RetMsg.RET_E301);
        }
        // 角色存在则更新
        packageRole(roleInfo, req);
        roleInfo.setRiUpdateBy(OP_NAME);
        roleInfoMapper.updateRole(roleInfo);
        log.info("添加的角色ID -> {}", roleInfo.getRiRoleId());
        // 角色赋权处理
        List<Integer> permIds = rolePermissionTableMapper.selectPermIdsByRoleId(roleInfo.getRiRoleId(), true);
        for (Integer permId : req.getPermList()) {
            // 若目标权限在原始权限中不存在则新增
            if (!permIds.contains(permId)) {
                RolePermissionTable rolePerm = packageRolePerm(roleInfo.getRiRoleId(), permId);
                rolePerm.setRptEnabled(Const.USER_ENABLED_TRUE);
                rolePerm.setRptCreateBy(OP_NAME);
                rolePerm.setRptUpdateBy(OP_NAME);
                rolePermissionTableMapper.saveRolePerm(rolePerm);
                log.info("新增角色权限 -> {}", rolePerm.getRptId());
            }
        }
        for (Integer permId : permIds) {
            // 若原始权限在目标权限中不存在则删除
            if (!req.getPermList().contains(permId)) {
                rolePermissionTableMapper.deleteRolePerm(roleInfo.getRiRoleId(), permId);
                log.info("删除角色权限 -> RoleId={}, PermId={}", roleInfo.getRiRoleId(), permId);
            }
        }
        return ResultUtil.retSuccess().fluentPut("roleId", roleInfo.getRiRoleId());
    }

    @Override
    public JSONObject deleteRoleAndPerm(String roleCode) {
        // 查询角色是否存在
        RoleInfo roleInfo = roleInfoMapper.selectByRoleCode(roleCode);
        if (roleInfo == null) {
            log.warn("该角色不存在 -> {}", roleCode);
            return ResultUtil.resp(RetMsg.RET_E301);
        }
        // 查询角色权限信息
        List<Integer> permIds = rolePermissionTableMapper.selectPermIdsByRoleId(roleInfo.getRiRoleId(), true);
        for (Integer permId : permIds) {
            // 删除已存在的权限信息
            rolePermissionTableMapper.deleteRolePerm(roleInfo.getRiRoleId(), permId);
            log.info("删除角色权限 -> RoleId={}, PermId={}", roleInfo.getRiRoleId(), permId);
        }
        // 删除角色信息
        roleInfoMapper.deleteByRoleId(roleInfo.getRiRoleId());
        log.info("删除角色 -> {}", roleInfo.getRiRoleId());
        return ResultUtil.retSuccess().fluentPut("roleId", roleInfo.getRiRoleId());
    }

    /**
     * 封装角色信息
     * @param src 原始数据
     * @param des 目标数据
     */
    private void packageRole(RoleInfo src, RolePermReq des) {
        src.setRiRoleCode(des.getRoleCode());
        src.setRiRoleDesc(des.getRoleDesc());
        src.setRiStatus(des.getRoleStat());
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
