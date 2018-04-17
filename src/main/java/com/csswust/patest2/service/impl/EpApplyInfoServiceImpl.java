package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.service.EpApplyInfoService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/17.
 */
@Service
public class EpApplyInfoServiceImpl extends BaseService implements EpApplyInfoService {
    @Autowired
    private EpApplyInfoDao epApplyInfoDao;
    @Autowired
    private EpUserInfoDao epUserInfoDao;

    @Override
    public APIResult selectByCondition(EpApplyInfo epApplyInfo, Integer page, Integer rows) {
        if (epApplyInfo == null) return null;
        APIResult apiResult = new APIResult();
        Integer total = epApplyInfoDao.selectByConditionGetCount(epApplyInfo, new BaseQuery());
        List<EpApplyInfo> epApplyInfoList = epApplyInfoDao.selectByCondition(epApplyInfo,
                new BaseQuery(page, rows));
        List<EpUserInfo> epUserInfoList = selectRecordByIds(
                getFieldByList(epApplyInfoList, "epUserId", EpApplyInfo.class),
                "userId", (BaseDao) epUserInfoDao, EpUserInfo.class);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("list", epApplyInfoList);
        apiResult.setDataKey("epUserInfoList", epUserInfoList);
        apiResult.setStatus(1);
        return apiResult;
    }
}
