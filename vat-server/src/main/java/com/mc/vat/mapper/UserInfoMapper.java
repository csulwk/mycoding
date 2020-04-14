package com.mc.vat.mapper;

import com.mc.vat.entity.UserInfo;
import com.mc.vat.entity.resp.UserRoleResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据处理
 * @author kai
 * @date 2020-4-4 15:19
 */
@Repository("userInfoMapper")
public interface UserInfoMapper {
    /**
     * 根据用户登录名称查询用户信息
     * @param username 用户登录名称
     * @return 用户信息
     */
    UserInfo selectByUserName(@Param("username") String username);

    List<UserRoleResp> getAllUserAllRole();

    /**
     * 获取所有用户信息
     * @return 用户信息
     */
    List<UserInfo> selectAllUserInfo();

    /**
     * 添加用户信息
     * @param userInfo 用户信息
     */
    void saveUser(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     */
    void updateUser(UserInfo userInfo);

    /**
     * 根据用户ID删除用户
     * @param userId 用户ID
     */
    void deleteByUserId(@Param("userId") Integer userId);
}
