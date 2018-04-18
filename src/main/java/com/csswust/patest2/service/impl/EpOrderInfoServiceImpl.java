package com.csswust.patest2.service.impl;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.EpOrderInfoService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    @Autowired
    private ExamInfoDao examInfoDao;

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

    @Transactional
    @Override
    public APIResult payment(Integer orderId) {
        APIResult apiResult = new APIResult();
        EpOrderInfo epOrderInfo = epOrderInfoDao.selectByPrimaryKey(orderId);
        if (epOrderInfo == null) {
            apiResult.setStatusAndDesc(-1, "账单无效");
            return apiResult;
        }
        EpApplyInfo epApplyInfo = epApplyInfoDao.selectByPrimaryKey(epOrderInfo.getApplyId());
        if (epApplyInfo == null) {
            apiResult.setStatusAndDesc(-2, "申请无效");
            return apiResult;
        }
        if (epOrderInfo.getIsPay() == 1) {
            apiResult.setStatusAndDesc(-3, "已付款，不能再次付款");
            return apiResult;
        }
        // 插入考试
        ExamInfo recordExam = buildExamInfo(epApplyInfo);
        int temp = examInfoDao.insertSelective(recordExam);
        if (temp != 1) {
            apiResult.setStatusAndDesc(-4, "插入考试数据失败：" +
                    JSON.toJSONString(recordExam));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } else {
            EpApplyInfo recordApply = new EpApplyInfo();
            recordApply.setApplyId(epApplyInfo.getApplyId());
            recordApply.setStatus(2);
            recordApply.setExamId(recordExam.getExamId());
            temp = epApplyInfoDao.updateByPrimaryKeySelective(recordApply);
            if (temp != 1) {
                apiResult.setStatusAndDesc(-5, "更新申请数据失败：" +
                        JSON.toJSONString(recordApply));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } else {
                EpOrderInfo recordOrder = new EpOrderInfo();
                recordOrder.setOrderId(epOrderInfo.getOrderId());
                recordOrder.setIsPay(1);
                recordOrder.setPayMoney(epOrderInfo.getMoney());
                temp = epOrderInfoDao.updateByPrimaryKeySelective(recordOrder);
                if (temp != 1) {
                    apiResult.setStatusAndDesc(-6, "更新订单数据失败：" +
                            JSON.toJSONString(recordOrder));
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                } else {
                    apiResult.setStatusAndDesc(1, "付款成功");
                }
            }
        }
        return apiResult;
    }

    private ExamInfo buildExamInfo(EpApplyInfo epApplyInfo) {
        ExamInfo recordExam = new ExamInfo();
        recordExam.setTitle(epApplyInfo.getExamName());
        recordExam.setDescription(epApplyInfo.getExamName());
        recordExam.setStartTime(epApplyInfo.getStartTime());
        recordExam.setEndTime(epApplyInfo.getEndTime());
        return recordExam;
    }
}
