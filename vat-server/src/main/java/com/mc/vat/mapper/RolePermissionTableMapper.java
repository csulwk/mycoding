package com.mc.vat.mapper;

import com.mc.vat.entity.RolePermissionTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限配置处理
 * @author kai
 * @date 2020-4-4 15:18
 */
@Repository("rolePermissionTableMapper")
public interface RolePermissionTableMapper {

    /**
     * 根据角色ID查询分配的权限
     * @param roleIds 角色ID
     * @return 权限ID
     */
    List<Integer> selectPermIdsByRoleIds(@Param("roleIds") List<Integer> roleIds);
}
