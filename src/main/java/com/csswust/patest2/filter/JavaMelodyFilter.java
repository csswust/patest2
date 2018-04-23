package com.csswust.patest2.filter;

import net.bull.javamelody.MonitoringFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by 972536780 on 2018/4/23.
 */
@WebFilter(
        filterName = "accessFilter",
        urlPatterns = "/monitoring/*")
public class JavaMelodyFilter extends MonitoringFilter {
    @Override
    public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) srequest;
        String uri = request.getRequestURI();
        if (uri == null) return;
        if (uri.contains("/monitoring")) {
            HttpSession session = request.getSession();
            String userPermisson = (String) session.getAttribute("userPermisson");
            if (userPermisson == null) {
                userPermisson = "not_login";
            }
            if (userPermisson.equals("admin")) return;
        }
        super.doFilter(srequest, sresponse, chain);
    }
}
