package com.mc.vat.mapper;

import com.mc.vat.entity.RoleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据处理
 * @author kai
 * @date 2020-4-4 15:18
 */
@Repository("roleInfoMapper")
public interface RoleInfoMapper {

    /**
     * 根据角色ID列表查询角色信息
     * @param roleIds 角色ID列表
     * @return 角色信息
     */
    List<RoleInfo> selectByRoleIds(@Param("roleIds") List<Integer> roleIds);

    /**
     * 获取所有角色信息
     * @return 角色信息
     */
    List<RoleInfo> selectAllRoleInfo();

    /**
     * 根据角色ID查询角色信息
     * @param roleId 角色ID
     * @return 角色信息
     */
    RoleInfo selectByRoleId(Integer roleId);

    /**
     * 根据角色代码查询角色信息
     * @param roleCode 角色代码
     * @return 角色信息
     */
    RoleInfo selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 添加新角色
     * @param roleInfo 角色信息
     */
    void saveRole(RoleInfo roleInfo);

    /**
     * 更新角色信息
     * @param roleInfo 角色信息
     */
    void updateRole(RoleInfo roleInfo);

    /**
     * 删除指定角色的信息
     * @param roleId 角色ID
     */
    void deleteByRoleId(@Param("roleId") Integer roleId);
}
