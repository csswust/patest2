package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.result.ExamPaperLoadRe;
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

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ExamPaper examPaper,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        Integer total = examPaperDao.selectByConditionGetCount(examPaper, new BaseQuery());
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, new BaseQuery(page, rows));
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "userId", ExamPaper.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        res.put("total", total);
        res.put("examPaperList", examPaperList);
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        return res;
    }

    @RequestMapping(value = "/uploadUserByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadUserByExamId(
            @RequestParam(required = true) Integer examId,
            @RequestParam(required = true) MultipartFile namefile,
            @RequestParam(required = false, defaultValue = "false") Boolean isIgnoreError) {
        Map<String, Object> res = new HashMap<>();
        ExamPaperLoadRe re = examPaperService.insertByExcel(namefile, examId, isIgnoreError);
        res.put("loadResult", re);
        return res;
    }
}
