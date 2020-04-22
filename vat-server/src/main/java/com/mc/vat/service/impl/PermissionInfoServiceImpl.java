package com.mc.vat.service.impl;

import com.mc.vat.entity.PermTree;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.mapper.PermissionInfoMapper;
import com.mc.vat.service.IPermissionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<PermTree> getRolePermListByRoleId(Integer roleId) {
        List<PermTree> permList = permissionInfoMapper.selectRolePermListByRoleId(roleId);
        log.info("查询参数 -> {}；查询结果 -> {}", roleId, permList);

        // 第一层菜单的pid=0
        List<PermTree> results = convertList2Tree(permList, 0);
        log.info("转换结果 -> {}", results);
        return results;
    }

    @Override
    public List<PermTree> getAllPermList() {
        List<PermTree> permList = permissionInfoMapper.selectAllPermList();
        log.info("权限列表 -> {}", permList);

        // 第一层菜单的pid=0
        List<PermTree> results = convertList2Tree(permList, 0);
        log.info("转换结果 -> {}", results);
        return results;
    }

    /**
     * 构建权限树
     * @param permList 权限列表
     * @param pid 权限ID
     * @return 权限树
     */
    private List<PermTree> convertList2Tree(List<PermTree> permList, Integer pid) {
        List<PermTree> child = new ArrayList<>();
        permList.forEach(p -> {
            if (Objects.equals(pid, p.getParentId())) {
                p.setChildren(convertList2Tree(permList, p.getPermId()));
                child.add(p);
            }
        });
        return child;
    }

}
