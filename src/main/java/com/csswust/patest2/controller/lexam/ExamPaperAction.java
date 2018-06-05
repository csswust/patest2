package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.OperateLogService;
import com.csswust.patest2.service.input.OperateLogInsert;
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
    private OperateLogService operateLogService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            ExamPaper examPaper,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyPaper,
            @RequestParam(required = false, defaultValue = "false") Boolean containOnline,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return examPaperService.selectByCondition(examPaper, onlyPaper, containOnline,
                userName, studentNumber, page, rows);
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
    public Object insertOne(@RequestParam Integer examId, @RequestParam String userName) {
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "插入单张试卷", examId);
        insert.setArgcData("userName", userName);
        operateLogService.insertOne(insert);
        return examPaperService.insertOne(examId, userName);
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(ExamPaper examPaper) {
        Map<String, Object> res = new HashMap<>();
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "更新试卷", examPaper.getExamId());
        insert.setArgcData("examPaper", examPaper);
        operateLogService.insertOne(insert);
        int result = examPaperDao.updateByPrimaryKeySelective(examPaper);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "删除试卷");
        insert.setArgcData("ids", ids);
        operateLogService.insertOne(insert);
        int result = examPaperDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteAllByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteAllByExamId(@RequestParam Integer examId) {
        APIResult apiResult = new APIResult();
        if (examId == null) {
            apiResult.setStatusAndDesc(-1, "examId不能为空");
            return apiResult;
        }
        int status = examPaperDao.deleteByExamId(examId);
        apiResult.setStatusAndDesc(status, status > 0 ? "成功" : "失败");
        return apiResult;
    }

    @RequestMapping(value = "/uploadUserByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object uploadUserByExamId(
            @RequestParam Integer examId,
            @RequestParam MultipartFile namefile,
            @RequestParam(required = false, defaultValue = "false") Boolean isIgnoreError) {
        if (examId == null) return new APIResult(-501, "examId不能为空");
        if (namefile == null) return new APIResult(-501, "namefile不能为空");
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "导入考试名单", examId);
        insert.setArgcData("examId", examId);
        operateLogService.insertOne(insert);
        return examPaperService.insertByExcel(namefile, examId, isIgnoreError);
    }

    @RequestMapping(value = "/drawProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Object drawProblem(@RequestParam Integer examId) {
        if (examId == null) return new APIResult(-501, "examId不能为空");
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "抽题", examId);
        insert.setArgcData("examId", examId);
        operateLogService.insertOne(insert);
        return examPaperService.drawProblemByExamId(examId, null);
    }
}
