package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.service.ExamParamService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;

/**
 * Created by 972536780 on 2018/3/20.
 */
@Service
public class ExamParamServiceImpl extends BaseService implements ExamParamService {
    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private ExamInfoDao examInfoDao;

    @Transactional
    @Override
    public APIResult insertByArray(Integer examId, Integer[] knowIds, Integer[] levels, Integer[] scores) {
        APIResult result = new APIResult();
        if (examId == null) {
            result.setStatus(-1);
            result.setDesc("考试id不能为空");
            return result;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            result.setStatus(-2);
            result.setDesc("当前考试不存在，可能已被删除");
            return result;
        }
        Date date = new Date();
        if (date.getTime() > examInfo.getEndTime().getTime()) {
            result.setStatus(-3);
            result.setDesc("考试已结束，不能修改参数信息");
            return result;
        }
        if (date.getTime() > examInfo.getStartTime().getTime()) {
            result.setStatus(-4);
            result.setDesc("考试进行中，不能修改参数信息");
            return result;
        }
        if (knowIds.length != levels.length || levels.length != scores.length) {
            result.setStatus(-5);
            result.setDesc("参数信息个数不匹配");
            return result;
        }
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);// 修复一个bug，以前修改其他考试会把当前的删掉
        List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery());
        List<Integer> exaParIds = getFieldByList(examParamList, "exaParId", ExamParam.class);
        int status = examParamDao.deleteByIdsList(exaParIds);
        int count = 0;
        for (int i = 0; i < knowIds.length; i++) {
            ExamParam temp = new ExamParam();
            temp.setExamId(examId);
            temp.setKnowId(knowIds[i]);
            temp.setLevelId(levels[i]);
            temp.setScore(scores[i]);
            int x = examParamDao.insertSelective(temp);
            if (x == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setStatus(-6);
                result.setDesc("插入失败");
                return result;
            }
            count = count + x;
        }
        ExamInfo record = new ExamInfo();
        examInfo.setExamId(examId);
        examInfoDao.updateByPrimaryKeySelective(record);
        result.setStatus(count);
        return result;
    }
}
