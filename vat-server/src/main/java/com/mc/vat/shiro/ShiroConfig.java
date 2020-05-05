package com.mc.vat.shiro;

import com.mc.vat.constant.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kai
 * @date 2020-04-01 1:14
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * cookie对象
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        log.info("ShiroConfig.rememberMeCookie...");
        // cookie的名称，对应前端的checkbox的name = remembered
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        // cookie生效时间2小时,单位秒
        simpleCookie.setMaxAge(14400);
        return simpleCookie;
    }

    /**
     * cookie管理对象，记住我功能
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        log.info("ShiroConfig.rememberMeManager...");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        // 设置加密的Key,参数类型byte[],字节数组长度要求16
        cookieRememberMeManager.setCipherKey("MYCODING_CRY_KEY".getBytes());
        return cookieRememberMeManager;
    }

    /**
     * 为防止密码在数据库里明码存储，通过该方法对密码进行编码；
     * 当然在登陆认证的时候,这个类也负责对表单输入的密码进行编码。
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        log.info("ShiroConfig.hashedCredentialsMatcher...");
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(Const.ENC_ALGORITHM);
        hashedCredentialsMatcher.setHashIterations(Const.ENC_TIMES);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义realm
     * @return ShiroRealm
     */
    @Bean
    public ShiroRealm shiroRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        log.info("ShiroConfig.shiroRealm...");
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return shiroRealm;
    }

    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm, CookieRememberMeManager rememberMeManager) {
        log.info("ShiroConfig.securityManager...");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 将自定义的realm交给SecurityManager管理
        securityManager.setRealm(shiroRealm);
        // 自定义缓存实现，使用redis
        // 自定义session管理，使用redis
        // 使用记住我功能
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        log.info("ShiroConfig.shiroFilter...");
        // 设置安全管理器
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // authc:所有url都必须认证通过才可以访问; anon:所有url都可以匿名访问
        Map<String, String> filterMap = new HashMap<>(8);
        filterMap.put("/mc/user/unlogged", "anon");
        filterMap.put("/mc/user/unauthorized", "anon");
        filterMap.put("/mc/user/login", "anon");
        filterMap.put("/auth/**", "anon");
        filterMap.put("/mc/user/logout", "authc");
        filterMap.put("/mc/user/info", "authc");
        filterMap.put("/**", "authc");

        shiroFilterFactoryBean.setLoginUrl("/mc/admin/unlogged");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        log.info("ShiroConfig.authorizationAttributeSourceAdvisor...");
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        log.info("ShiroConfig.lifecycleBeanPostProcessor...");
        return new LifecycleBeanPostProcessor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        log.info("ShiroConfig.defaultAdvisorAutoProxyCreator...");
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}
