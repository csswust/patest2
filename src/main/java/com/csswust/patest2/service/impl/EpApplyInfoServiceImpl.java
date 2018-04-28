package com.csswust.patest2.service.impl;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.service.EpApplyInfoService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Calendar;
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
    @Autowired
    private EpOrderInfoDao epOrderInfoDao;

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
        List<EpOrderInfo> epOrderInfoList = selectRecordByIds(
                getFieldByList(epApplyInfoList, "orderId", EpApplyInfo.class),
                "orderId", (BaseDao) epOrderInfoDao, EpOrderInfo.class);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("list", epApplyInfoList);
        apiResult.setDataKey("epUserInfoList", epUserInfoList);
        apiResult.setDataKey("epOrderInfoList", epOrderInfoList);
        apiResult.setStatus(1);
        return apiResult;
    }

    @Transactional
    @Override
    public APIResult accept(Integer applyId, Integer status, Double money, String reason) {
        APIResult apiResult = new APIResult();
        if (status != 0 && status != 1) {
            apiResult.setStatusAndDesc(-501, "参数非法");
            return apiResult;
        }
        EpApplyInfo epApplyInfo = epApplyInfoDao.selectByPrimaryKey(applyId);

        if (epApplyInfo == null) {
            apiResult.setStatusAndDesc(-1, "applyId无效");
            return apiResult;
        }
        if (epApplyInfo.getStatus() == 1 || epApplyInfo.getStatus() == 2) {
            apiResult.setStatusAndDesc(-2, "此申请已通过");
            return apiResult;
        }
        // status等于0，-1才允许通过
        if (epApplyInfo.getStatus() != 0 && epApplyInfo.getStatus() != -1) {
            apiResult.setStatusAndDesc(-3, "无法修改");
            return apiResult;
        }
        EpApplyInfo record = new EpApplyInfo();
        record.setApplyId(applyId);
        record.setStatus(status == 1 ? 1 : -1);
        record.setReason(reason);
        int result = epApplyInfoDao.updateByPrimaryKeySelective(record);
        if (result == 1) {
            if (status == 1) {
                // 添加订单
                EpOrderInfo epOrderInfo = new EpOrderInfo();
                epOrderInfo.setApplyId(applyId);
                epOrderInfo.setDescription(epApplyInfo.getExamName());
                epOrderInfo.setEpUserId(epApplyInfo.getEpUserId());
                epOrderInfo.setIsPay(0);
                epOrderInfo.setMoney(money);

                Calendar now = Calendar.getInstance();
                String examYear = String.format("%d", now.get(Calendar.YEAR));
                String examDay = String.format("%1$02d", now.get(Calendar.DAY_OF_MONTH));
                String examMoth = String.format("%1$02d", now.get(Calendar.MONTH) + 1);
                String newexamId = String.format("%1$06d", epApplyInfo.getApplyId());
                String newSysId = String.format("%1$06d", epApplyInfo.getEpUserId());
                epOrderInfo.setOrderNum(examYear + examMoth + examDay + newSysId + newexamId);
                int temp = epOrderInfoDao.insertSelective(epOrderInfo);
                if (temp == 0) {
                    apiResult.setStatusAndDesc(-4, "插入订单失败" + getJson(epOrderInfo));
                } else {
                    EpApplyInfo recordApply = new EpApplyInfo();
                    recordApply.setApplyId(record.getApplyId());
                    recordApply.setOrderId(epOrderInfo.getOrderId());
                    temp = epApplyInfoDao.updateByPrimaryKeySelective(recordApply);
                    if (temp == 0) {
                        apiResult.setStatusAndDesc(-5, "更新申请失败" + getJson(recordApply));
                    } else {
                        apiResult.setStatusAndDesc(temp, "审核成功");
                    }
                }
            } else {
                apiResult.setStatusAndDesc(result, "拒绝成功");
            }
        } else {
            apiResult.setStatusAndDesc(-5, "更新申请失败" + getJson(record));
        }
        if (apiResult.getStatus() != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return apiResult;
    }

    @Override
    public APIResult deleteById(Integer applyId) {
        APIResult apiResult = new APIResult();
        int result = epApplyInfoDao.deleteByPrimaryKey(applyId);
        if (result == 1) apiResult.setStatusAndDesc(1, "删除成功");
        else apiResult.setStatusAndDesc(-1, "删除失败");
        return apiResult;
    }
}
