package com.csswust.patest2.common.config;

import com.csswust.patest2.entity.VisitPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2017/11/20.
 */
public class AuthConfig {
    private static long lastLoading = 0;
    private static Map<String, VisitPath> authConfig = new HashMap<>();

    public static boolean isAuth(String urlPath) {
        return true;
    }

    public static void loadingAuth() {
        if (System.currentTimeMillis() - lastLoading <= Config.getToInt(SiteKey.AUTH_TABLE_LOADING_INTERVAL,
                SiteKey.AUTH_TABLE_LOADING_INTERVAL_DEFAULT)) {
            return;
        }

    }
}
