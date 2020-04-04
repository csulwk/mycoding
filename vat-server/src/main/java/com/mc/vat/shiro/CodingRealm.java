package com.mc.vat.shiro;

import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.mapper.UserInfoMapper;
import com.mc.vat.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kai
 * @date 2020-04-01 0:41
 */
@Slf4j
public class CodingRealm extends AuthorizingRealm {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 权限验证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("doGetAuthorizationInfo -> {}", principalCollection.toString());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        log.info("登录用户 -> {}", user);

        //此处把当前subject对应的所有角色信息交给shiro，调用hasRole的时候就根据这些role信息判断
        List<RoleInfo> roles = userInfoService.getRoleInfoByUsername(user.getUiUsername());
        log.info("角色验证 -> {}", roles);
        Set<String> roleCodes = new HashSet<>(roles.size());
        for (RoleInfo role : roles) {
            roleCodes.add(role.getRiRoleCode());
        }
        authorizationInfo.setRoles(roleCodes);

        //此处把当前subject对应的权限信息交给shiro，当调用hasPermission的时候就会根据这些信息判断
        List<PermissionInfo> perms = userInfoService.getPermissionInfoByUsername(user.getUiUsername());
        log.info("权限验证 -> {}", perms);
        Set<String> permCodes = new HashSet<>(perms.size());
        for (PermissionInfo perm : perms) {
            permCodes.add(perm.getPiPermCode());
        }
        authorizationInfo.setStringPermissions(permCodes);
        return authorizationInfo;
    }

    /**
     * 登录验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("doGetAuthenticationInfo -> {}", authenticationToken.toString());

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = (String) authenticationToken.getPrincipal();
        UserInfo user = userInfoService.getUserInfoByUsername(username);
        log.info("登录验证 -> {}", user);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        //authenticationInfo信息交个shiro，调用login的时候会自动比较这里的token和authenticationInfo
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,
                user.getUiPassword(), this.getName());
        return authenticationInfo;
    }
}
