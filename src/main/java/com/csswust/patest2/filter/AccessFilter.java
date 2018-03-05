package com.csswust.patest2.filter;

import com.csswust.patest2.common.service.AuthService;
import com.csswust.patest2.common.service.SpringUtilService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户权限过滤
 *
 * @author 杨顺丰
 */
@WebFilter(filterName = "accessFilter", urlPatterns = {"/user/*"})
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getServletPath();
        HttpSession session = req.getSession();
        String userPermisson = (String) session.getAttribute("userPermisson");
        if (userPermisson == null) {
            userPermisson = "not_login";
        }
        if ("admin".equals(userPermisson) || "teacher".equals(userPermisson)) {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AuthService authService = SpringUtilService.getBean("authService");
            if (authService.isAuth(url, userPermisson)) {
                try {
                    chain.doFilter(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.sendRedirect("/patest2/system/authError");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("权限过滤器初始化......");
    }

    @Override
    public void destroy() {
        System.out.println("权限过滤器销毁......");
    }
}
