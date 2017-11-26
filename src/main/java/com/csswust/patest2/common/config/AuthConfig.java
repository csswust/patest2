package com.csswust.patest2.common.config;

import com.csswust.patest2.entity.VisitPath;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2017/11/20.
 */
@Component
public class AuthConfig {
    public static boolean isAuth(String urlPath, String name) {
        return true;
    }
}
