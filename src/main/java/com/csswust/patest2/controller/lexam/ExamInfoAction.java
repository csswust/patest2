package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.result.SelectProblemNumRe;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamInfoService;
import com.csswust.patest2.service.common.ConditionBuild;
import com.csswust.patest2.service.result.ImportDataRe;
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
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/examInfo")
public class ExamInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamInfoAction.class);

    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private ConditionBuild conditionBuild;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ExamInfo examInfo,
            @RequestParam(required = false, defaultValue = "false") Boolean isRecent,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyExamInfo,
            @RequestParam(required = false, defaultValue = "false") Boolean containUModify,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examInfo == null) return null;
        Map<String, Object> res = new HashMap<>();
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
        res.put("total", total);
        res.put("examInfoList", examInfoList);
        if (onlyExamInfo) {
            return res;
        }
        List<Integer> peopleTotal = new ArrayList<>();
        List<Integer> statusList = new ArrayList<>();
        List<Integer> proState = new ArrayList<>();
        ExamPaper examPaper = new ExamPaper();
        Date time = new Date();
        for (int i = 0; i < examInfoList.size(); i++) {
            ExamInfo item = examInfoList.get(i);
            examPaper.setExamId(item.getExamId());
            int temp = examPaperDao.selectByConditionGetCount(examPaper, new BaseQuery());
            peopleTotal.add(temp);
            Integer status;
            if (time.getTime() > item.getEndTime().getTime()) {
                status = 2;
            } else if (time.getTime() > item.getStartTime().getTime()) {
                status = 1;
            } else {
                status = 0;
            }
            statusList.add(status);
            PaperProblem paperProblem = new PaperProblem();
            paperProblem.setExamId(item.getExamId());
            int size = paperProblemDao.selectByConditionGetCount(paperProblem, new BaseQuery());
            proState.add(size == 0 ? 0 : 1);
        }
        if (containUModify) {
            List<UserInfo> userInfoList = selectRecordByIds(
                    getFieldByList(examInfoList, "modifyUserId", ExamInfo.class),
                    "userId", (BaseDao) userInfoDao, UserInfo.class);
            List<UserProfile> userProfileList = selectRecordByIds(
                    getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                    "useProId", (BaseDao) userProfileDao, UserProfile.class);
            res.put("userInfoList", userInfoList);
            res.put("userProfileList", userProfileList);
        }
        res.put("peopleTotal", peopleTotal);
        res.put("statusList", statusList);
        res.put("proState", proState);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(ExamInfo examInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.insertSelective(examInfo);
        res.put("status", result);
        res.put("examId", examInfo.getExamId());
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(ExamInfo examInfo) {
        return examInfoService.updateById(examInfo);
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/selectMyProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectMyProblem(@RequestParam Integer examId) {
        if (examId == null) return null;
        Map<String, Object> res = new HashMap<>();
        List<SelectProblemNumRe> selectProblemNumReList =
                paperProblemDao.selectProblemNum(examId);
        List<Integer> probIdList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < selectProblemNumReList.size(); i++) {
            probIdList.add(selectProblemNumReList.get(i).getProbId());
            countList.add(selectProblemNumReList.get(i).getNum());
        }
        List<ProblemInfo> problemInfoList = selectRecordByIds(probIdList,
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        List<KnowledgeInfo> knowledgeInfoList = selectRecordByIds(
                getFieldByList(problemInfoList, "knowId", ProblemInfo.class),
                "knowId", (BaseDao) knowledgeInfoDao, KnowledgeInfo.class);
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        res.put("probIdList", probIdList);
        res.put("countList", countList);
        res.put("problemInfoList", problemInfoList);
        res.put("knowledgeInfoList", knowledgeInfoList);
        res.put("courseInfoList", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/rankingByGrade", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> rankingByGrade(
            @RequestParam Integer examId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examId == null) return null;
        Map<String, Object> res = new HashMap<>();
        ExamPaper examPaper = new ExamPaper();
        examPaper.setExamId(examId);
        BaseQuery baseQuery = new BaseQuery();
        conditionBuild.buildExamPaper(baseQuery, examPaper, userName, studentNumber);
        Integer total = examPaperDao.selectByConditionGetCount(examPaper, baseQuery);
        baseQuery.setPageRows(page, rows);
        baseQuery.setCustom("sort", "sort");
        List<ExamPaper> allExamPaperList = examPaperDao.selectByCondition(examPaper, baseQuery);
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(allExamPaperList, "userId", ExamPaper.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<List<PaperProblem>> PaperProblemList = new ArrayList<>();
        List<List<ProblemInfo>> ProblemInfoList = new ArrayList<>();
        PaperProblem paperProblem = new PaperProblem();
        int problemTotal = 0;
        for (ExamPaper item : allExamPaperList) {
            paperProblem.setExamPaperId(item.getExaPapId());
            List<PaperProblem> paperProblems = paperProblemDao.selectByCondition(paperProblem, new BaseQuery());
            List<ProblemInfo> problemInfos = selectRecordByIds(
                    getFieldByList(paperProblems, "problemId", PaperProblem.class),
                    "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
            if (paperProblems.size() > problemTotal) {
                problemTotal = paperProblems.size();
            }
            PaperProblemList.add(paperProblems);
            ProblemInfoList.add(problemInfos);
        }
        res.put("total", total);
        res.put("examPaperList", allExamPaperList);
        res.put("PaperProblemList", PaperProblemList);
        res.put("ProblemInfoList", ProblemInfoList);
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        res.put("problemTotal", problemTotal);
        return res;
    }

    @RequestMapping(value = "/importCodeByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public ImportDataRe importCodeByExamId(@RequestParam Integer examId) {
        if (examId == null) return null;
        return examInfoService.importCodeByExamId(examId);
    }

    @RequestMapping(value = "/importGradeByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public ImportDataRe importGradeByExamId(@RequestParam Integer examId) {
        if (examId == null) return null;
        return examInfoService.importGradeByExamId(examId);
    }
}
