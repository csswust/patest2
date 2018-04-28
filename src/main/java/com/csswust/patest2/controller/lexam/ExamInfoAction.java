package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.ExamInfoService;
import com.csswust.patest2.service.OperateLogService;
import com.csswust.patest2.service.input.OperateLogInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/examInfo")
public class ExamInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamInfoAction.class);

    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            ExamInfo examInfo,
            @RequestParam(required = false, defaultValue = "false") Boolean isRecent,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyExamInfo,
            @RequestParam(required = false, defaultValue = "false") Boolean containUModify,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examInfo == null) return null;
        APIResult apiResult = new APIResult();
        BaseQuery baseQuery = new BaseQuery();
        if (examInfo.getStartTime() != null) {
            baseQuery.setCustom("startTime", examInfo.getStartTime());
            examInfo.setStartTime(null);
        }
        if (examInfo.getEndTime() != null) {
            baseQuery.setCustom("endTime", examInfo.getEndTime());
            examInfo.setEndTime(null);
        }
        if (isRecent) {
            baseQuery.setCustom("isRecent", true);
        }
        Integer total = examInfoDao.selectByConditionGetCount(examInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ExamInfo> examInfoList = examInfoDao.selectByCondition(examInfo, baseQuery);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("examInfoList", examInfoList);
        if (onlyExamInfo) {
            return apiResult;
        }
        examInfoService.buildExamState(apiResult, examInfoList, containUModify);
        return apiResult;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(ExamInfo examInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.insertSelective(examInfo);
        res.put("status", result);
        res.put("examId", examInfo.getExamId());
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "添加考试", examInfo.getExamId());
        insert.setArgcData("examInfo", examInfo);
        operateLogService.insertOne(insert);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(ExamInfo examInfo) {
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "修改考试", examInfo.getExamId());
        insert.setArgcData("examInfo", examInfo);
        operateLogService.insertOne(insert);
        return examInfoService.updateById(examInfo);
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "删除考试");
        insert.setArgcData("ids", ids);
        operateLogService.insertOne(insert);
        int result = examInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/selectMyProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectMyProblem(@RequestParam Integer examId) {
        return examInfoService.selectMyProblem(examId);
    }

    @RequestMapping(value = "/rankingByGrade", method = {RequestMethod.GET, RequestMethod.POST})
    public Object rankingByGrade(@RequestParam Integer examId,
                                 @RequestParam(required = false) String userName, @RequestParam(required = false) String studentNumber,
                                 @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer rows) {
        return examInfoService.rankingByGrade(examId, userName, studentNumber, page, rows);
    }

    @RequestMapping(value = "/importCodeByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object importCodeByExamId(@RequestParam Integer examId) {
        return examInfoService.importCodeByExamId(examId);
    }

    @RequestMapping(value = "/importGradeByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object importGradeByExamId(@RequestParam Integer examId) {
        return examInfoService.importGradeByExamId(examId);
    }
}
