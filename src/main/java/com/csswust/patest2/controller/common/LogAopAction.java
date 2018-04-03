package com.csswust.patest2.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by 972536780 on 2018/4/3.
 */
@Aspect
public class LogAopAction {
    private static Logger log = LoggerFactory.getLogger(LogAopAction.class);


    @Pointcut("execution(* com.csswust.patest2.controller.*.*(..))")
    private void controllerAspect() {
    }


    @Before("controllerAspect()")
    public void controller(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Long count = Stream.of(signature.getMethod().getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType() == RequestMapping.class)
                .count();
        String requestPath = count >= 1 ? signature.getMethod().getAnnotation(RequestMapping.class).value()[0] : "";
        String info = String.format("path:%s | %s", requestPath, getMethodInfo(point));
        log.info(info);
    }

    private String getMethodInfo(JoinPoint point) {
        String className = point.getSignature().getDeclaringType().getSimpleName();
        String methodName = point.getSignature().getName();
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        Map<String, Object> map = new HashMap<>();
        if (Objects.nonNull(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                // 去除文件类型
                if ("namefile".equals(parameterNames[i])) continue;
                map.put(parameterNames[i], point.getArgs()[i]);
            }
        }
        String argsJson = "";
        try {
            argsJson = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            log.error("JSON.toJSONString error: {}", e);
        }
        return String.format("class:%s | method:%s | args:%s", className, methodName, argsJson);
    }
}
