package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.service.EpOrderInfoService;
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
public class EpOrderInfoServiceImpl extends BaseService implements EpOrderInfoService {
    @Autowired
    private EpOrderInfoDao epOrderInfoDao;
    @Autowired
    private EpUserInfoDao epUserInfoDao;
    @Autowired
    private EpApplyInfoDao epApplyInfoDao;

    @Override
    public APIResult selectByCondition(EpOrderInfo epOrderInfo, Integer page, Integer rows) {
        if (epOrderInfo == null) return null;
        APIResult apiResult = new APIResult();
        List<EpOrderInfo> epOrderInfoList = epOrderInfoDao.selectByCondition(epOrderInfo,
                new BaseQuery(page, rows));
        List<EpUserInfo> epUserInfoList = selectRecordByIds(
                getFieldByList(epOrderInfoList, "epUserId", EpOrderInfo.class),
                "userId", (BaseDao) epUserInfoDao, EpUserInfo.class);
        List<EpApplyInfo> epApplyInfoList = selectRecordByIds(
                getFieldByList(epOrderInfoList, "applyId", EpOrderInfo.class),
                "applyId", (BaseDao) epApplyInfoDao, EpApplyInfo.class);
        Integer total = epOrderInfoDao.selectByConditionGetCount(epOrderInfo, new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("list", epOrderInfoList);
        apiResult.setDataKey("epUserInfoList", epUserInfoList);
        apiResult.setDataKey("epApplyInfoList", epApplyInfoList);
        apiResult.setStatus(1);
        return apiResult;
    }
}
