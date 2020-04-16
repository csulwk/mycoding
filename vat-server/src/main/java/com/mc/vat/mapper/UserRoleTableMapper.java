package com.mc.vat.mapper;

import com.mc.vat.entity.UserRoleTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色数据处理
 * @author kai
 * @date 2020-4-4 15:20
 */
@Repository("userRoleTableMapper")
public interface UserRoleTableMapper {

    /**
     * 根据用户ID查询分配的角色
     * @param userIds 用户ID
     * @return 角色ID
     */
    List<Integer> selectRoleIdsByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 根据用户ID查询分配的角色
     * @param userId 用户ID
     * @return 角色ID
     */
    List<Integer> selectRoleIdsByUserId(@Param("userId") Integer userId);

    /**
     * 添加用户角色信息
     * @param userRole 角色信息
     */
    void saveUserRole(UserRoleTable userRole);

    /**
     * 更新用户角色信息
     * @param userRole 角色信息
     */
    void updateUserRole(UserRoleTable userRole);

    /**
     * 查询用户角色信息
     * @param userId 用户角色ID
     * @return 用户角色信息
     */
    UserRoleTable selectUserRoleByUserId(@Param("userId") Integer userId);
}
