package com.mc.vat.shiro;

import com.mc.vat.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author kai
 * @date 2020-04-01 0:41
 */
@Slf4j
public class CodingRealm extends AuthorizingRealm {

    /**
     * 权限验证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("doGetAuthorizationInfo -> {}", principalCollection.toString());

        return new SimpleAuthorizationInfo();
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
        String username = token.getUsername();
        log.info("登录用户：{}", username);
        log.info("登录密码：{}", token.getPassword());
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setEmail("admin@qq.com");
        admin.setMotto("I am BOSS ~");
        if (admin == null) {
            throw new RuntimeException("用户不存在");
        }
        return new SimpleAuthenticationInfo(admin, admin.getPassword(), this.getName());
    }
}
