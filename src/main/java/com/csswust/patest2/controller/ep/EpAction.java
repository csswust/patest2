package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/ep")
public class EpAction extends BaseAction {
    @Autowired
    private SiteInfoDao siteInfoDao;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;

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
}
