package com.csswust.patest2.common.cache.impl;

import com.csswust.patest2.common.SpringUtil;
import com.csswust.patest2.common.cache.Cache;
import com.csswust.patest2.common.cache.CacheLoader;
import com.csswust.patest2.dao.VisitPathDao;
import com.csswust.patest2.entity.VisitPath;

/**
 * Created by 972536780 on 2017/11/26.
 */
public class VisitPathCache extends Cache<String, VisitPath> {
    private static VisitPathCache loader = new VisitPathCache(new VisitPathCacheLoader());

    public static VisitPathCache getInstance() {
        return loader;
    }

    private VisitPathCache(CacheLoader<String, VisitPath> cacheLoader) {
        super(cacheLoader);
    }

    private final static class VisitPathCacheLoader implements CacheLoader<String, VisitPath> {
        private VisitPathDao visitPathDao = SpringUtil.getBean("visitPathDao");

        @Override
        public VisitPath load(String key) throws Exception {
            return null;
        }
    }
}
