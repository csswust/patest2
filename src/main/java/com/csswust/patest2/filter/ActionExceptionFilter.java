package com.csswust.patest2.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by 972536780 on 2018/3/14.
 */
@WebFilter(filterName = "actionExceptionFilter", urlPatterns = "/*")
public class ActionExceptionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("异常处理过滤器初始化......");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        System.out.println("异常处理过滤器销毁......");
    }
}
