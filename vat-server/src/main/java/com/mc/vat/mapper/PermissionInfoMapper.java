package com.mc.vat.mapper;

import com.mc.vat.entity.PermissionInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限数据处理
 * @author kai
 * @date 2020-4-4 15:16
 */
@Repository("permissionInfoMapper")
public interface PermissionInfoMapper {
    /**
     * 根据权限ID列表查询权限信息
     * @param permIds 权限ID
     * @return 权限信息
     */
    List<PermissionInfo> selectByPermIds(@Param("permIds") List<Integer> permIds);

    /**
     * 获取所有权限信息
     * @return 权限信息
     */
    List<PermissionInfo> selectAllPermissionInfo();
}