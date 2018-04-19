package com.csswust.patest2.filter;

import com.csswust.patest2.common.UserRole;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by 972536780 on 2018/4/19.
 */
public class BaseFilter {
    protected String geturl(HttpServletRequest req) {
        // 获取请求地址url
        String contextPath = req.getContextPath();
        String URI = req.getRequestURI();
        return URI.replace(contextPath, "");
    }

    protected String getPermisson(HttpSession session) {
        String userPermisson = (String) session.getAttribute("userPermisson");
        if (userPermisson == null) {
            userPermisson = "not_login";
        }
        return userPermisson;
    }

    protected Integer getUserId(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            String userPermisson = (String) session.getAttribute("userPermisson");
            if (userPermisson != null && userPermisson.equals(UserRole.EP_USER.getPermisson())) {
                userId = (Integer) session.getAttribute("epUserId");
            }
        }
        MDC.put("userId", userId == null ? "not_login" : String.valueOf(userId));
        return userId;
    }
}
