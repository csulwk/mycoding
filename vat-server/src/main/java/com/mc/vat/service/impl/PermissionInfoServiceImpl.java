package com.mc.vat.service.impl;

import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.mapper.PermissionInfoMapper;
import com.mc.vat.service.IPermissionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限处理
 * @author kai
 * @date 2020-04-04 16:42
 */
@Service("permissionInfoService")
@Slf4j
public class PermissionInfoServiceImpl implements IPermissionInfoService {

    private PermissionInfoMapper permissionInfoMapper;
    @Autowired
    public PermissionInfoServiceImpl(PermissionInfoMapper permissionInfoMapper) {
        this.permissionInfoMapper = permissionInfoMapper;
    }

    @Override
    public List<PermissionInfo> getAllPermissionInfo() {
        List<PermissionInfo> results = permissionInfoMapper.selectAllPermissionInfo();
        log.info("无参查询结果 -> {}", results);
        return results;
    }

    @Override
    public List<PermissionInfo> getPermissionListByRoleIds(List<Integer> roleIds) {
        List<PermissionInfo> results = permissionInfoMapper.selectByPermIds(roleIds);
        log.info("查询参数 -> {}；查询结果 -> {}", roleIds, results);
        return results;
    }
}
