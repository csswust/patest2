package com.csswust.patest2.common.service;

import com.csswust.patest2.common.UserRole;
import com.csswust.patest2.common.cache.Cache;
import com.csswust.patest2.common.cache.impl.VisitPathCache;
import com.csswust.patest2.entity.VisitPath;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by 972536780 on 2017/11/20.
 */
@Service
public class AuthService {
    private static Cache<String, VisitPath> cache = VisitPathCache.getInstance();

    public boolean isAuth(String urlPath, String name) {
        if (StringUtils.isBlank(urlPath) || StringUtils.isBlank(name)) {
            return false;
        }
        VisitPath visitPath = cache.get(urlPath);
        UserRole userRole = UserRole.getByName(name);
        if (visitPath == null || userRole == null) {
            return false;
        }
        String ids = visitPath.getRoleIds();
        return StringUtils.isNotBlank(ids) && ids.contains(String.valueOf(userRole.getId()));
    }
}
