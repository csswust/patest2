package com.csswust.patest2.controller.common;

import com.csswust.patest2.common.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    protected void removeSession(HttpServletRequest request, String paramName) {
        HttpSession session = request.getSession();
        session.removeAttribute(paramName);
    }

    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }


    protected String getIp() {
        return this.request.getRemoteAddr();
    }

    protected Integer getUserId() {
        HttpSession session = request.getSession();
        return (Integer) session.getAttribute("userId");
    }

    protected String getUrl() {
        // 获取请求地址url
        String contextPath = request.getContextPath();
        String URI = request.getRequestURI();
        return URI.replace(contextPath, "");
    }

    protected Integer getEpUserId() {
        HttpSession session = request.getSession();
        Object object = session.getAttribute("epUserId");
        return object == null ? -1 : (Integer) session.getAttribute("epUserId");
    }
}
