package com.csswust.patest2.controller.common;

import com.csswust.patest2.common.Base;
import com.csswust.patest2.common.paramJudge.ParamCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

public class BaseAction extends Base {
    private static Logger log = LoggerFactory.getLogger(BaseAction.class);
    // 这样是线程安全的
    @Autowired(required = false)
    public HttpServletRequest request;

    @Autowired(required = false)
    public HttpServletResponse response;

    public void saveSession(HttpServletRequest request, Map<String, Object> param) {
        HttpSession session = request.getSession();
        for (Entry<String, Object> entry : param.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public void saveSession(HttpServletRequest request, String paramName, Object value) {
        HttpSession session = request.getSession();
        session.setAttribute(paramName, value);
    }

    public Object getSession(HttpServletRequest request, String paramName) {
        HttpSession session = request.getSession();
        return session.getAttribute(paramName);
    }

    public void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    public String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public Integer getUserId() {
        HttpSession session = request.getSession();
        return (Integer) session.getAttribute("userId");
    }


    public <T> T paramVerificate(T param, ParamCallBack callBacks) {
        if (param == null) {
            return null;
        }
        Class<?> clz = param.getClass();
        // 获取类中的全部定义字段
        Field[] fields = clz.getDeclaredFields();
        // 循环遍历字段，获取字段相应的属性值
        for (Field field : fields) {
            // 假设不为空。设置可见性，然后返回
            field.setAccessible(true);
            try {
                Object value = field.get(param);
                if (callBacks.judgeType(value)) {
                    field.set(param, callBacks.replaceParam(value));
                }
            } catch (Exception e) {
                log.error("BaseAction.paramVerificate({}, {}) error: {}", param, callBacks, e);
            }
        }
        return param;
    }
}
