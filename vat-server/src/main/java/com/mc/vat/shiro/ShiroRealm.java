package com.mc.vat.shiro;

import com.mc.vat.constant.Const;
import com.mc.vat.entity.PermissionInfo;
import com.mc.vat.entity.RoleInfo;
import com.mc.vat.entity.UserInfo;
import com.mc.vat.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录判断
 * @author kai
 * @date 2020-04-01 0:41
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 权限验证
     * @param principalCollection principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("doGetAuthorizationInfo -> {}", principalCollection.toString());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        log.info("登录用户 -> {}", user);
        SecurityUtils.getSubject().getSession().setAttribute("username", user.getUiUsername());

        // 此处把当前subject对应的所有角色信息交给shiro，调用hasRole的时候就根据这些role信息判断
        List<RoleInfo> roles = userInfoService.getRoleInfoByUsername(user.getUiUsername());
        log.info("角色验证 -> {}", roles);
        Set<String> roleCodes = new HashSet<>(roles.size());
        for (RoleInfo role : roles) {
            roleCodes.add(role.getRiRoleCode());
        }
        authorizationInfo.setRoles(roleCodes);
        SecurityUtils.getSubject().getSession().setAttribute("roles", roleCodes);

        // 此处把当前subject对应的权限信息交给shiro，当调用hasPermission的时候就会根据这些信息判断
        List<PermissionInfo> perms = userInfoService.getPermissionInfoByUsername(user.getUiUsername());
        log.info("权限验证 -> {}", perms);
        Set<String> permCodes = new HashSet<>(perms.size());
        for (PermissionInfo perm : perms) {
            permCodes.add(perm.getPiPermCode());
        }
        authorizationInfo.setStringPermissions(permCodes);
        SecurityUtils.getSubject().getSession().setAttribute("perms", permCodes);
        return authorizationInfo;
    }

    /**
     * 登录验证
     * @param authenticationToken authenticationToken
     * @return SimpleAuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("doGetAuthenticationInfo -> {}", authenticationToken.toString());
        // 获取用户输入的账号密码
        String username = authenticationToken.getPrincipal().toString();
        // 获取数据库中的账号密码
        UserInfo user = userInfoService.getUserInfoByUsername(username);
        log.info("登录验证 -> {}", user);
        if (ObjectUtils.isEmpty(user)) {
            log.info("用户不存在: {}", username);
            throw new UnknownAccountException();
        }
        if (Const.USER_ENABLED_FALSE.equals(user.getUiStatus())) {
            log.info("用户已失效: {}", username);
            throw new LockedAccountException();
        }
        // authenticationInfo信息交给shiro，调用login的时候会自动比较这里的token和authenticationInfo
        // 通过配置中的 HashedCredentialsMatcher 进行自动校验
        return new SimpleAuthenticationInfo(user, user.getUiPassword(), ByteSource.Util.bytes(user.getUiSalt()), this.getName());
    }
}
