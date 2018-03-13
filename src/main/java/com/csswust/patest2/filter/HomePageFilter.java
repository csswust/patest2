package com.csswust.patest2.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/index.html")
public class HomePageFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.sendRedirect("/patest/html/login.html");
	}

	public void destroy() {}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
}
