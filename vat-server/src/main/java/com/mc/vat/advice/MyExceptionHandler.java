package com.mc.vat.advice;

import com.alibaba.fastjson.JSONObject;
import com.mc.vat.constant.RetMsg;
import com.mc.vat.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 自定义拦截器
 * @author kai
 * @date 2020-04-06 9:40
 */
@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @SuppressWarnings("unchecked")
    @ExceptionHandler(value = AuthorizationException.class)
    public JSONObject unauthorizedHandler(HttpServletRequest req, Exception exp) {
        String username = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
        Set<String> roles = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute("roles");
        log.info("用户[{}]角色 -> {}", username, roles);
        Set<String> perms = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute("perms");
        log.info("用户[{}]权限 -> {}", username, perms);
        String url = req.getRequestURI();
        log.info("用户[{}]请求[{}]异常 -> {}", username, url, exp.getMessage());
        return ResultUtil.resp(RetMsg.RET_E203);
    }
}
