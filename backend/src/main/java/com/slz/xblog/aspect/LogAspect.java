package com.slz.xblog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * @author slz
 * @date 2023/7/13
 * 记录日志
 */

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    HttpServletRequest request;

    @After("execution(* com.slz.xblog.controller.*.*(..))") // 根据你的控制器包路径调整
    public void log(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        try {
            // 获取类对象
            Class<?> clazz = Class.forName(className);

            // 获取类上的 @RequestMapping
            RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
            String classPath = (classRequestMapping != null && classRequestMapping.value().length > 0)
                    ? classRequestMapping.value()[0] : "";

            // 遍历类的方法并匹配方法名称
            String methodPath = "";
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    methodPath = getMethodPath(method);
                    break;
                }
            }

            // 拼接完整的路径
            String fullPath = classPath + methodPath;

            // 记录访问信息
            log.info(request.getRemoteHost() + "：访问路径：" + fullPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getMethodPath(Method method) {
        String path = "";

        // 检查不同的请求映射注解
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        GetMapping getMapping = method.getAnnotation(GetMapping.class);

        if (requestMapping != null && requestMapping.value().length > 0) {
            path = requestMapping.value()[0];
        } else if (postMapping != null && postMapping.value().length > 0) {
            path = postMapping.value()[0];
        } else if (getMapping != null && getMapping.value().length > 0) {
            path = getMapping.value()[0];
        }

        return path;
    }
}

