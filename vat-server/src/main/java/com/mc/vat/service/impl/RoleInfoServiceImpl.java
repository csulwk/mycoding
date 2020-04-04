package com.mc.vat.service.impl;

import com.mc.vat.entity.RoleInfo;
import com.mc.vat.mapper.RoleInfoMapper;
import com.mc.vat.service.IRoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    public RoleInfoServiceImpl(RoleInfoMapper roleInfoMapper) {
        this.roleInfoMapper = roleInfoMapper;
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
}
