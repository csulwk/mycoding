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
     * 根据角色ID查询角色信息
     * @param roleIds 角色ID
     * @return 角色信息
     */
    List<RoleInfo> selectByRoleIds(@Param("roleIds") List<Integer> roleIds);

    /**
     * 获取所有角色信息
     * @return 角色信息
     */
    List<RoleInfo> selectAllRoleInfo();
}
