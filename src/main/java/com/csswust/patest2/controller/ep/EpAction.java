package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/ep")
public class EpAction extends BaseAction {
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private UserProfileDao userProfileDao;

    @RequestMapping(value = "/selectEpSite", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectEpSite() {
        APIResult apiResult = new APIResult();
        String[] array = new String[]{
                "ep_serv_people", "ep_serv_concern", "ep_principal",
                "ep_telephone", "ep_address", "ep_team_info", "ep_email"
        };
        for (int i = 0; i < array.length; i++) {
            apiResult.setDataKey(array[i], Config.get(array[i]));
        }
        apiResult.setStatus(1);
        return apiResult;
    }

    @RequestMapping(value = "/selectExamTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectExamTotal() {
        APIResult apiResult = new APIResult();
        int total = examInfoDao.selectByConditionGetCount(new ExamInfo(), new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setStatus(1);
        return apiResult;
    }

    @RequestMapping(value = "/selectUserTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectUserTotal() {
        APIResult apiResult = new APIResult();
        int total = userInfoDao.selectByConditionGetCount(new UserInfo(), new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setStatus(1);
        return apiResult;
    }

    @RequestMapping(value = "/selectProblemAllCount", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectProblemAllCount() {
        APIResult apiResult = new APIResult();
        int total = problemInfoDao.selectByConditionGetCount(new ProblemInfo(), new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setStatus(1);
        return apiResult;
    }

    @RequestMapping(value = "/selectKnowledgeAllCount", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectKnowledgeAllCount() {
        APIResult apiResult = new APIResult();
        int total = knowledgeInfoDao.selectByConditionGetCount(new KnowledgeInfo(), new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setStatus(1);
        return apiResult;
    }

    @RequestMapping(value = "/selectExamGradeByUserName", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectExamGradeByUserName(@RequestParam String username) {
        APIResult apiResult = new APIResult();
        if (StringUtils.isBlank(username)) {
            apiResult.setStatusAndDesc(-501, "username不能为空");
            return apiResult;
        }
        UserInfo userInfo = userInfoDao.selectByUsername(username);
        if (userInfo == null || userInfo.getUserId() == null) {
            apiResult.setStatusAndDesc(-1, "此用户不存在");
            return apiResult;
        }
        userInfo.setPassword(null);
        apiResult.setDataKey("userInfo", userInfo);
        UserProfile userProfile = userProfileDao.selectByPrimaryKey(userInfo.getUserProfileId());
        if (userProfile == null) userProfile = new UserProfile();
        apiResult.setDataKey("userProfile", userProfile);
        ExamPaper examPaper = new ExamPaper();
        examPaper.setUserId(userInfo.getUserId());
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, new BaseQuery());
        List<ExamInfo> examInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "examId", ExamPaper.class),
                "examId", (BaseDao) examInfoDao, ExamInfo.class);
        if (examPaperList != null && examPaperList.size() != 0) {
            apiResult.setStatusAndDesc(1, "查询成功");
            apiResult.setDataKey("examPaperList", examPaperList);
            apiResult.setDataKey("examInfoList", examInfoList);
        } else {
            apiResult.setStatusAndDesc(0, "无认证记录");
        }
        return apiResult;
    }
}
