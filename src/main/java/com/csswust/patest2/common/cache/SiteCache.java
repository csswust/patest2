package com.csswust.patest2.common.cache;

import com.csswust.patest2.dao.JudgerInfoDao;
import com.csswust.patest2.dao.ResultInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.JudgerInfo;
import com.csswust.patest2.entity.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/1.
 */
@Component
public class SiteCache {
    public static Integer resultInfoTotal;
    public static List<ResultInfo> resultInfoList;

    public static Integer judgerInfoTotal;
    public static List<JudgerInfo> judgerInfoList;

    @Autowired
    private ResultInfoDao resultInfoDao;
    @Autowired
    private JudgerInfoDao judgerInfoDao;

    public void refresh() {
        BaseQuery baseQuery = new BaseQuery();
        ResultInfo resultInfo = new ResultInfo();
        resultInfoList = resultInfoDao.selectByCondition(resultInfo, baseQuery);
        resultInfoTotal = resultInfoDao.selectByConditionGetCount(resultInfo, baseQuery);
        JudgerInfo judgerInfo = new JudgerInfo();
        judgerInfoList = judgerInfoDao.selectByCondition(judgerInfo, baseQuery);
        judgerInfoTotal = judgerInfoDao.selectByConditionGetCount(judgerInfo, baseQuery);
    }
}
