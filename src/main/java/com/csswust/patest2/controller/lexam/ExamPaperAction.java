package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.OnlineUserService;
import com.csswust.patest2.service.common.ConditionBuild;
import com.csswust.patest2.service.result.DrawProblemRe;
import com.csswust.patest2.service.result.ExamPaperLoadRe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/21.
 */
@RestController
@RequestMapping("/examPaper")
public class ExamPaperAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamPaperAction.class);

    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private ConditionBuild conditionBuild;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ExamPaper examPaper,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyPaper,
            @RequestParam(required = false, defaultValue = "false") Boolean containOnline,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examPaper == null) return null;
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        conditionBuild.buildExamPaper(baseQuery, examPaper, userName, studentNumber);
        Integer total = examPaperDao.selectByConditionGetCount(examPaper, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, baseQuery);
        res.put("total", total);
        res.put("examPaperList", examPaperList);
        if (onlyPaper) {
            return res;
        }
        List<Integer> userIds = getFieldByList(examPaperList, "userId", ExamPaper.class);
        List<UserInfo> userInfoList = selectRecordByIds(userIds,
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<ExamInfo> examInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "examId", ExamPaper.class),
                "examId", (BaseDao) examInfoDao, ExamInfo.class);
        if (containOnline) {
            List<String> sessinoList = onlineUserService.judgeOnline(userIds);
            res.put("sessinoList", sessinoList);
        }
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        res.put("examInfoList", examInfoList);
        return res;
    }

    @RequestMapping(value = "/selectPaperById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectPaperById(@RequestParam Integer exaPapId) {
        if (exaPapId == null) return null;
        Map<String, Object> res = new HashMap<>();
        ExamPaper examPaper = examPaperDao.selectByPrimaryKey(exaPapId);
        if (examPaper == null) {
            return res;
        }
        PaperProblem paperProblem = new PaperProblem();
        paperProblem.setExamPaperId(exaPapId);
        List<PaperProblem> paperProblemList = paperProblemDao.selectByCondition(paperProblem, new BaseQuery());
        List<ProblemInfo> problemInfoList = selectRecordByIds(
                getFieldByList(paperProblemList, "problemId", PaperProblem.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        List<SubmitInfo> submitInfoList = selectRecordByIds(
                getFieldByList(paperProblemList, "submitId", PaperProblem.class),
                "submId", (BaseDao) submitInfoDao, SubmitInfo.class);
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(examPaper.getUserId());
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        UserProfile userProfile = userProfileDao.selectByPrimaryKey(userInfo.getUserProfileId());
        if (userProfile == null) {
            userProfile = new UserProfile();
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examPaper.getExamId());
        if (examInfo == null) examInfo = new ExamInfo();
        res.put("examPaper", examPaper);
        res.put("paperProblemList", paperProblemList);
        res.put("problemInfoList", problemInfoList);
        res.put("userInfo", userInfo);
        res.put("userProfile", userProfile);
        res.put("submitInfoList", submitInfoList);
        res.put("examInfo", examInfo);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(@RequestParam Integer examId, @RequestParam String userName) {
        if (examId == null || StringUtils.isBlank(userName)) return null;
        Map<String, Object> res = new HashMap<>();
        UserInfo userInfo = userInfoDao.selectByUsername(userName);
        if (userInfo == null) {
            res.put("status", -1);
            res.put("desc", "用户不存在");
        } else {
            ExamPaper examPaper = new ExamPaper();
            examPaper.setExamId(examId);
            examPaper.setUserId(userInfo.getUserId());
            examPaper.setAcedCount(0);
            examPaper.setScore(0.0);
            examPaper.setIsMarked(0);
            int status = examPaperDao.insertSelective(examPaper);
            if (status == 1) {
                UserInfo record = new UserInfo();
                record.setUserId(userInfo.getUserId());
                record.setExamId(examId);
                userInfoDao.updateByPrimaryKeySelective(userInfo);
            }
            res.put("status", status);
        }
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(ExamPaper examPaper) {
        Map<String, Object> res = new HashMap<>();
        int result = examPaperDao.updateByPrimaryKeySelective(examPaper);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examPaperDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/uploadUserByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadUserByExamId(
            @RequestParam Integer examId,
            @RequestParam MultipartFile namefile,
            @RequestParam(required = false, defaultValue = "false") Boolean isIgnoreError) {
        if (examId == null) return null;
        if (namefile == null) return null;
        Map<String, Object> res = new HashMap<>();
        ExamPaperLoadRe re = examPaperService.insertByExcel(namefile, examId, isIgnoreError);
        res.put("loadResult", re);
        return res;
    }

    @RequestMapping(value = "/drawProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> drawProblem(
            @RequestParam Integer examId) {
        if (examId == null) return null;
        Map<String, Object> res = new HashMap<>();
        DrawProblemRe re = examPaperService.drawProblemByExamId(examId, null);
        res.put("drawProblemRe", re);
        return res;
    }
}
