package com.csswust.patest2.common.cache;

import com.csswust.patest2.dao.JudgerInfoDao;
import com.csswust.patest2.dao.ResultInfoDao;
import com.csswust.patest2.dao.VisitPathDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.JudgerInfo;
import com.csswust.patest2.entity.ResultInfo;
import com.csswust.patest2.entity.VisitPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/4/1.
 */
@Component
public class SiteCache {
    public static Integer resultInfoTotal;
    public static List<ResultInfo> resultInfoList;

    public static Integer judgerInfoTotal;
    public static List<JudgerInfo> judgerInfoList;

    public static Map<String, VisitPath> visitPathMap = new HashMap<>();

    @Autowired
    private ResultInfoDao resultInfoDao;
    @Autowired
    private JudgerInfoDao judgerInfoDao;
    @Autowired
    private VisitPathDao visitPathDao;

    public void refresh() {
        BaseQuery baseQuery = new BaseQuery();
        ResultInfo resultInfo = new ResultInfo();
        resultInfoList = resultInfoDao.selectByCondition(resultInfo, baseQuery);
        resultInfoTotal = resultInfoDao.selectByConditionGetCount(resultInfo, baseQuery);
        JudgerInfo judgerInfo = new JudgerInfo();
        judgerInfoList = judgerInfoDao.selectByCondition(judgerInfo, baseQuery);
        judgerInfoTotal = judgerInfoDao.selectByConditionGetCount(judgerInfo, baseQuery);
        List<VisitPath> visitPathList = visitPathDao.selectByCondition(new VisitPath(), baseQuery);
        for (VisitPath visitPath : visitPathList) {
            visitPathMap.put(visitPath.getUrl(), visitPath);
        }
    }
}
