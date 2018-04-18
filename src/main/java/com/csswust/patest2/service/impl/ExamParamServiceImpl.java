package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.service.ExamParamService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/20.
 */
@Service
public class ExamParamServiceImpl extends BaseService implements ExamParamService {
    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;

    @Override
    public APIResult selectByCondition(ExamParam examParam, Integer page, Integer rows) {
        APIResult apiResult = new APIResult();
        Integer total = examParamDao.selectByConditionGetCount(examParam, new BaseQuery());
        List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery(page, rows));
        List<KnowledgeInfo> knowledgeInfoList = selectRecordByIds(
                getFieldByList(examParamList, "knowId", ExamParam.class),
                "knowId", (BaseDao) knowledgeInfoDao, KnowledgeInfo.class);
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        List<Integer> problemSumList = new ArrayList<>();
        for (int i = 0; i < examParamList.size(); i++) {
            ExamParam temp = new ExamParam();
            temp.setExamId(examParamList.get(i).getExamId());
            temp.setKnowId(examParamList.get(i).getKnowId());
            temp.setLevelId(examParamList.get(i).getLevelId());
            int count = examParamDao.getProblemNum(temp, new BaseQuery());
            problemSumList.add(count);
        }
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("examParamList", examParamList);
        apiResult.setDataKey("problemSumList", problemSumList);
        apiResult.setDataKey("knowledgeInfoList", knowledgeInfoList);
        apiResult.setDataKey("courseInfoList", courseInfoList);
        return apiResult;
    }

    @Override
    public APIResult selectProblemTotal(Integer knowId, Integer levelId, Integer examId) {
        APIResult apiResult = new APIResult();
        ExamParam temp = new ExamParam();
        temp.setExamId(examId);
        temp.setKnowId(knowId);
        temp.setLevelId(levelId);
        int count = examParamDao.getProblemNum(temp, new BaseQuery());
        apiResult.setDataKey("total", count);
        return apiResult;
    }

    @Transactional
    @Override
    public APIResult insertByArray(Integer examId, Integer[] knowIds, Integer[] levels, Integer[] scores) {
        APIResult result = new APIResult();
        if (examId == null) {
            result.setStatusAndDesc(-1, "考试id不能为空");
            return result;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            result.setStatusAndDesc(-2, "当前考试不存在，可能已被删除");
            return result;
        }
        Date date = new Date();
        if (date.getTime() > examInfo.getEndTime().getTime()) {
            result.setStatusAndDesc(-3, "考试已结束，不能修改参数信息");
            return result;
        }
        if (date.getTime() > examInfo.getStartTime().getTime()) {
            result.setStatusAndDesc(-4, "考试进行中，不能修改参数信息");
            return result;
        }
        if (knowIds.length != levels.length || levels.length != scores.length) {
            result.setStatusAndDesc(-5, "参数信息个数不匹配");
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
                result.setStatusAndDesc(-6, "插入失败");
                return result;
            }
            count = count + x;
        }
        ExamInfo record = new ExamInfo();
        examInfo.setExamId(examId);
        examInfoDao.updateByPrimaryKeySelective(record);
        result.setStatusAndDesc(count, "插入成功");
        return result;
    }

    @Override
    public APIResult deleteByIds(String ids) {
        APIResult apiResult = new APIResult();
        int result = examParamDao.deleteByIds(ids);
        if (result == 0) {
            apiResult.setStatusAndDesc(-1, "删除失败");
        } else {
            apiResult.setStatusAndDesc(result, "删除成功");
        }
        return apiResult;
    }

    @Override
    public APIResult deleteById(Integer id) {
        APIResult apiResult = new APIResult();
        int result = examParamDao.deleteByPrimaryKey(id);
        if (result == 0) {
            apiResult.setStatusAndDesc(-1, "删除失败");
        } else {
            apiResult.setStatusAndDesc(result, "删除成功");
        }
        return apiResult;
    }
}
