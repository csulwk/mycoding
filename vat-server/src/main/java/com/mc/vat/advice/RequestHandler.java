package com.mc.vat.advice;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求打印处理
 * @author kai
 * @date 2020-04-11 16:23
 */
@Slf4j
@Aspect
@Component
public class RequestHandler {

    /**
     * RequestMapping请求注解作为切点"@annotation(org.springframework.web.bind.annotation.RequestMapping)"
     * Controller请求方法作为切点"execution(public * com.mc.vat.controller..*.*(..))"
     */
    @Pointcut("execution(public * com.mc.vat.controller..*.*(..))")
    public void requestMappingMethod() {}

    /**
     * 环绕，可以在切点前后织入代码，并且可以自由的控制何时执行切点
     * @param proceedingJoinPoint
     * @return
     */
    @Around("requestMappingMethod()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 执行切点
        Object respObj = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        // 打印响应日志
        log.info("响应信息: {}", JSONObject.toJSONString(respObj));
        log.info("请求耗时: {} ms", endTime - startTime);
        return respObj;
    }

    /**
     * 在切点之前，织入相关代码
     * @param joinPoint
     */
    @Before("requestMappingMethod()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] args = joinPoint.getArgs();
        // 返回的数组中携带有Request(HttpServletRequest)或者Response(HttpServletResponse)对象，导致序列化异常
        Stream<?> stream = ArrayUtils.isEmpty(args) ? Stream.empty() : Arrays.asList(args).stream();
        List<Object> logArgs = stream.filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());
        // 打印请求日志
        log.info("请求开始...");
        log.info("请求链接: {}", request.getRequestURL().toString());
        log.info("请求参数: {}", JSONObject.toJSONString(logArgs));
        log.info("请求类型: {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("请求方法: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的IP
        log.info("请求地址: {}", request.getRemoteAddr());
    }

    /**
     * 在切点之后，织入相关代码
     */
    @After("requestMappingMethod()")
    public void doAfter() {
        log.info("请求结束...");
    }

}
