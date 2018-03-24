package com.csswust.patest2.spring;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 972536780 on 2018/3/24.
 */
public class MyMultipartResolver extends CommonsMultipartResolver {
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (url != null && url.contains("/system/ueditor")) {
            return false;
        } else {
            return super.isMultipart(request);
        }
    }
}
