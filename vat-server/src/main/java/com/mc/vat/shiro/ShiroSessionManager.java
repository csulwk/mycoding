package com.mc.vat.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author kai
 * @date 2020-05-05 18:01
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "Authorization";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        log.info("ShiroSessionManager.getSessionId...");
        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if (StringUtils.isEmpty(sessionId)) {
            //如果没有携带 Authorization 参数则按照父类的方式在cookie进行获取
            log.info("no sessionId -> sessionId: {}", super.getSessionId(request, response));
            return super.getSessionId(request, response);
        } else {
            //如果请求头中有 AUTHORIZATION 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            log.info("has sessionId -> sessionId: {}", sessionId);
            return sessionId;
        }

    }

}
