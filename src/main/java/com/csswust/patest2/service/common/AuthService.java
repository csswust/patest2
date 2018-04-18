package com.csswust.patest2.service.common;

import com.csswust.patest2.common.UserRole;
import com.csswust.patest2.common.cache.SiteCache;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.VisitPath;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 972536780 on 2017/11/20.
 */
@Component
public class AuthService extends BaseService {
    public boolean isAuth(String urlPath, String name) {
        if (StringUtils.isBlank(urlPath) || StringUtils.isBlank(name)) {
            return false;
        }
        VisitPath visitPath = SiteCache.visitPathMap.get(urlPath);
        UserRole userRole = UserRole.getByName(name);
        if (visitPath == null || userRole == null) {
            return false;
        }
        String ids = visitPath.getRoleIds();
        return StringUtils.isNotBlank(ids) && ids.contains(String.valueOf(userRole.getId()));
    }

    @Autowired
    private EpApplyInfoDao epApplyInfoDao;

    public boolean judgeEpAuth(Integer epUserId, Integer examId) {
        if (epUserId == null || examId == null) return false;
        EpApplyInfo epApplyInfo = new EpApplyInfo();
        epApplyInfo.setEpUserId(epUserId);
        epApplyInfo.setExamId(examId);
        List<EpApplyInfo> epApplyInfoList = epApplyInfoDao.selectByCondition(epApplyInfo, new BaseQuery());
        if (epApplyInfoList == null || epApplyInfoList.size() == 0) return false;
        for (EpApplyInfo item : epApplyInfoList) {
            if (item == null) continue;
            if (item.getEpUserId().intValue() == epUserId.intValue()
                    && item.getExamId().intValue() == examId.intValue()) {
                return true;
            }
        }
        return false;
    }
}
