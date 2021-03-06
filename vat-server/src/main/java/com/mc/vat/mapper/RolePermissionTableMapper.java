package com.mc.vat.mapper;

import com.mc.vat.entity.RolePermissionTable;
import com.mc.vat.entity.page.RolePageResp;
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
     * @return 权限ID列表
     */
    List<Integer> selectPermIdsByRoleIds(@Param("roleIds") List<Integer> roleIds);

    /**
     * 根据角色ID查询分配的权限
     * @param roleId 角色ID
     * @param includeParent 是否包括父节点
     * @return 权限ID列表
     */
    List<Integer> selectPermIdsByRoleId(@Param("roleId") Integer roleId, @Param("includeParent") Boolean includeParent);

    /**
     * 根据权限ID查询所属的角色
     * @param permId 权限ID
     * @return 角色ID列表
     */
    List<Integer> selectRoleIdsByPermId(@Param("permId") Integer permId);

    /**
     * 新增角色权限信息
     * @param rolePerm 角色权限信息
     */
    void saveRolePerm(RolePermissionTable rolePerm);

    /**
     * 删除角色权限信息
     * @param roleId 角色ID
     * @param permId 权限ID
     */
    void deleteRolePerm(@Param("roleId") Integer roleId, @Param("permId") Integer permId);

    /**
     * 根据角色ID和权限ID查询角色权限信息
     * @param roleId 角色ID
     * @param permId 权限ID
     * @return 角色权限信息
     */
    RolePermissionTable selectByRoleIdAndPermId(@Param("roleId") Integer roleId, @Param("permId") Integer permId);

    /**
     * 分页查询角色权限信息
     * @param roleId 角色ID
     * @return 分页数据
     */
    List<RolePageResp> selectRolePermInfoByRoleId(@Param("roleId") Integer roleId);
}
