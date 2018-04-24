package com.csswust.patest2.filter;

import com.csswust.patest2.common.MonitorKey;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.service.common.AuthService;
import com.csswust.patest2.service.common.SpringUtilService;
import com.csswust.patest2.service.monitor.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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
@WebFilter(
        filterName = "accessFilter",
        urlPatterns = {"/academyInfo/*", "/courseInfo/*", "/examInfo/*",
                "/examNotice/*", "/examPaper/*", "/examParam/*", "/examProblem/*",
                "/judgerInfo/*", "/knowledgeInfo/*", "/majorInfo/*",
                "/paperProblem/*", "/problemInfo/*", "/resultInfo/*",
                "/siteInfo/*", "/student/*", "/submitInfo/*", "/submitResult/*",
                "/submitSimilarity/*", "/system/*", "/userInfo/*", "/userProfile/*",
                "/epApplyInfo/*", "/epNotice/*", "/epOrderInfo/*", "/ep/*"
        })
public class AccessFilter extends BaseFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Integer isAuthJudge = Config.getToInt(SiteKey.IS_AUTH_JUDGE, SiteKey.IS_AUTH_JUDGE_DE);
        // 获取请求地址url
        HttpServletRequest req = (HttpServletRequest) request;
        String url = geturl(req);
        // 获取用户角色和userId
        HttpSession session = req.getSession();
        String userPermisson = getPermisson(session);
        // 开始
        Integer userId = getUserId(session);
        long startTime = System.currentTimeMillis();

        Monitor monitor = SpringUtilService.getBean("monitor");
        monitor.addCount(MonitorKey.SYSTEM_REQUEST_CONCURRENCY.getKey(), 1);
        if (isAuthJudge != 1) {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                log.error("chain.doFilter data:{} error: {}", url, e);
            }
        } else {
            AuthService authService = SpringUtilService.getBean("authService");
            if (authService.isAuth(url, userPermisson)) {
                try {
                    chain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("chain.doFilter data:{} error: {}", url, e);
                }
            } else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.sendRedirect("/patest/system/authError");
            }
        }
        long endTime = System.currentTimeMillis();
        monitor.addSize(MonitorKey.SYSTEM_REQUEST_TIME.getKey(), (int) (endTime - startTime));
        MDC.remove("userId");
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
