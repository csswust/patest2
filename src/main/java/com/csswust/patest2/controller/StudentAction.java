package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.listener.ApplicationStartListener;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.utils.ArrayUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/22.
 */
@RestController
@RequestMapping("/student")
public class StudentAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(StudentAction.class);

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private SubmitResultDao submitResultDao;
    @Autowired
    private ExamPaperService examPaperService;

    @RequestMapping(value = "/selectMyProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectMyProblem(@RequestParam Integer examId) {
        Map<String, Object> res = new HashMap<>();
        Integer userId = getUserId();
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(userId);
        if (userId == null || examId == null || userInfo == null) {
            return res;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        //判断是否开始
        Date nowTime = new Date();
        if (examInfo == null || nowTime.getTime() < examInfo.getStartTime().getTime()) {
            return res;
        }
        //获取该考生的试卷(包括考生的ac量，分数)
        ExamPaper examPaperCondition = new ExamPaper();
        examPaperCondition.setExamId(examId);
        examPaperCondition.setUserId(userInfo.getUserId());
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaperCondition, new BaseQuery());
        if (examPaperList == null || examPaperList.size() == 0) {
            return res;
        }
        ExamPaper examPaper = examPaperList.get(0);
        PaperProblem record = new PaperProblem();
        record.setExamPaperId(examPaper.getExaPapId());
        //取出该试卷的所有提交记录
        List<PaperProblem> paperProblemList = paperProblemDao.selectByCondition(record, new BaseQuery());
        // 如果paperProblemList==null，那么当前用户未抽题
        if (paperProblemList == null || paperProblemList.size() == 0) {
            examPaperService.drawProblemByExamId(examId, userId);
            paperProblemList = paperProblemDao.selectByCondition(record, new BaseQuery());
        }
        List<ExamParam> examParamList = new ArrayList<>();
        List<ProblemInfo> problemInfoList = new ArrayList<>();
        int sumScore = 0;
        for (int i = 0; i < paperProblemList.size(); i++) {
            ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(paperProblemList.get(i).getProblemId());
            problemInfo.setStandardSource(null);
            ExamParam examParam = examParamDao.selectByPrimaryKey(paperProblemList.get(i).getExamParamId());
            problemInfoList.add(problemInfo);
            examParamList.add(examParam);
            sumScore += examParam.getScore();
        }
        //考生的试卷(考生的ac量，分数)
        res.put("examPaper", examPaper);
        res.put("sumScore", sumScore);
        res.put("problemInfoList", problemInfoList);
        //考生的所有提交记录
        res.put("paperProblemList", paperProblemList);
        //考生的题库列表
        res.put("problemInfoList", problemInfoList);
        //试卷的参数
        res.put("examParamList", examParamList);
        res.put("status", 1);
        return res;
    }

    @RequestMapping(value = "/selectProblemByPapProId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectProblemByPapProId(@RequestParam Integer papProId) {
        Map<String, Object> res = new HashMap<>();
        PaperProblem paperProblem = paperProblemDao.selectByPrimaryKey(papProId);
        if (paperProblem == null) {
            return res;
        }
        ExamPaper examPaper = examPaperDao.selectByPrimaryKey(paperProblem.getExamPaperId());
        if (examPaper == null) {
            return res;
        }
        // 判断是否登录
        Integer userId = getUserId();
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(userId);
        if (userInfo == null) {
            return res;
        }
        //判断是否是本人的试卷
        if (userId.intValue() != examPaper.getUserId().intValue()) {
            return res;
        }
        ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(paperProblem.getProblemId());
        if (problemInfo == null) {
            return res;
        }
        if (userInfo.getIsActive() != 1 || (userInfo.getIsAdmin() != 1 && userInfo.getIsTeacher() != 1)) {
            problemInfo.setStandardSource(null);
        }
        res.put("status", 1);
        res.put("problemInfo", problemInfo);
        PaperProblem temp = new PaperProblem();
        temp.setExamPaperId(paperProblem.getExamPaperId());
        List<PaperProblem> paperProblemList = paperProblemDao.selectByCondition(temp, new BaseQuery());
        for (int i = 0; i < paperProblemList.size(); i++) {
            PaperProblem index = paperProblemList.get(i);
            if (index.getPapProId().intValue() == paperProblem.getPapProId().intValue()) {
                if (i - 1 < 0) {
                    res.put("preId", null);
                } else {
                    res.put("preId", paperProblemList.get(i - 1).getPapProId());
                }
                if (i + 1 >= paperProblemList.size()) {
                    res.put("nextId", null);
                } else {
                    res.put("nextId", paperProblemList.get(i + 1).getPapProId());
                }
                break;
            }
        }
        return res;
    }

    @RequestMapping(value = "/insertSubmitInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertSubmitInfo(SubmitInfo submitInfo) {
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isBlank(submitInfo.getSource())
                || submitInfo.getJudgerId() == null
                || submitInfo.getPaperProblemId() == null) {
            res.put("status", -500);
            res.put("desc", "参数非法");
            return res;
        }
        // 判断是否登录
        Integer userId = getUserId();
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(userId);
        if (userInfo == null) {
            res.put("status", -1);
            res.put("desc", "未登录");
            return res;
        }
        submitInfo.setUserId(userId);
        submitInfo.setIp(getIp(request));
        submitInfo.setIsTeacherTest(0);
        submitInfo.setStatus(11);
        PaperProblem paperProblem = paperProblemDao.selectByPrimaryKey(submitInfo.getPaperProblemId());
        if (paperProblem == null) {
            res.put("status", -2);
            res.put("desc", "未找到试卷题目");
            return res;
        }
        submitInfo.setExamId(paperProblem.getExamId());
        submitInfo.setExamPaperId(paperProblem.getExamPaperId());
        submitInfo.setExamParamId(paperProblem.getExamParamId());
        submitInfo.setProblemId(paperProblem.getProblemId());
        ExamPaper examPaper = examPaperDao.selectByPrimaryKey(paperProblem.getExamPaperId());
        if (examPaper == null) {
            res.put("status", -3);
            res.put("desc", "未找到试卷");
            return res;
        }
        if (examPaper.getUserId().intValue() != submitInfo.getUserId().intValue()) {
            res.put("status", -4);
            res.put("desc", "不能帮助别人提交");
            return res;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examPaper.getExamId());
        if (examInfo == null) {
            res.put("status", -5);
            res.put("desc", "考试不存在");
            return res;
        }
        Date nowTime = new Date();
        if (nowTime.getTime() < examInfo.getStartTime().getTime()) {
            res.put("status", -6);
            res.put("desc", "考试还未开始");
            return res;
        }
        if (nowTime.getTime() > examInfo.getEndTime().getTime()) {
            res.put("status", -7);
            res.put("desc", "考试已结束");
            return res;
        }
        ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(submitInfo.getProblemId());
        if (problemInfo == null || submitInfo.getSource().length() * 2 > problemInfo.getCodeLimit()) {
            res.put("status", -8);
            res.put("desc", "代码长度超出限制");
            return res;
        }
        int result = submitInfoDao.insertSelective(submitInfo);
        res.put("status", result);
        res.put("submId", submitInfo.getSubmId());
        if (submitInfo.getSubmId() != null) {
            ApplicationStartListener.queue.add(submitInfo.getSubmId());
        }
        return res;
    }

    @RequestMapping(value = "/selectMySubmit", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            SubmitInfo submitInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        // 判断是否登录
        Integer userId = getUserId();
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(userId);
        if (userInfo == null) {
            res.put("status", -1);
            res.put("desc", "未登录");
            return res;
        }
        submitInfo.setUserId(userId);
        BaseQuery baseQuery = new BaseQuery();
        Integer total = submitInfoDao.selectByConditionGetCount(submitInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(submitInfo, baseQuery);
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(submitInfoList, "userId", SubmitInfo.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<ProblemInfo> problemInfoList = selectRecordByIds(
                getFieldByList(submitInfoList, "problemId", SubmitInfo.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        for (int i = 0; i < problemInfoList.size(); i++) {
            problemInfoList.get(i).setStandardSource(null);
        }
        res.put("total", total);
        res.put("submitInfoList", submitInfoList);
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        res.put("problemInfoList", problemInfoList);
        return res;
    }

    @RequestMapping(value = "/selectDetailResult", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(@RequestParam Integer submId) {
        Map<String, Object> res = new HashMap<>();
        // 判断是否登录
        Integer userId = getUserId();
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(userId);
        if (userInfo == null) {
            res.put("status", -1);
            res.put("desc", "未登录");
            return res;
        }
        SubmitInfo submitInfo = submitInfoDao.selectByPrimaryKey(submId);
        if (submitInfo == null) {
            res.put("status", -2);
            res.put("desc", "提交不存在");
            return res;
        }
        if (userId.intValue() != submitInfo.getUserId().intValue()) {
            res.put("status", -3);
            res.put("desc", "只能查看本人的结果");
            return res;
        }
        BaseQuery baseQuery = new BaseQuery();
        SubmitResult submitResult = new SubmitResult();
        submitResult.setSubmitId(submId);
        List<SubmitResult> submitResultList = submitResultDao.selectByCondition(
                submitResult, baseQuery);
        ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(submitInfo.getProblemId());
        if (problemInfo == null) {
            res.put("status", -4);
            res.put("desc", "问题信息为空");
            return res;
        }
        List<Integer> scoreRatioList = null;
        try {
            scoreRatioList = ArrayUtil.StringToArray(problemInfo.getScoreRatio(), ",");
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.put("scoreRatioList", scoreRatioList);
        res.put("submitResultList", submitResultList);
        return res;
    }
}
