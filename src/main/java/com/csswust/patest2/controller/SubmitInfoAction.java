package com.csswust.patest2.controller;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.SubmitInfo;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.listener.ApplicationStartListener;
import org.apache.commons.lang3.StringUtils;
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

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/submitInfo")
public class SubmitInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(SubmitInfoAction.class);

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            SubmitInfo submitInfo,
            @RequestParam(required = false, defaultValue = "false") boolean onlySubmitInfo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (submitInfo == null) return null;
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isNotBlank(username)) {
            UserInfo userInfo = userInfoDao.selectByUsername(username);
            submitInfo.setUserId(userInfo == null ? -1 : userInfo.getUserId());
        }
        BaseQuery baseQuery = new BaseQuery();
        Integer total = submitInfoDao.selectByConditionGetCount(submitInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(submitInfo, baseQuery);
        res.put("total", total);
        res.put("submitInfoList", submitInfoList);
        if (onlySubmitInfo) {
            return res;
        }
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(submitInfoList, "userId", SubmitInfo.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<ProblemInfo> problemInfoList = selectRecordByIds(
                getFieldByList(submitInfoList, "problemId", SubmitInfo.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        res.put("userInfoList", userInfoList);
        res.put("userProfileList", userProfileList);
        res.put("problemInfoList", problemInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(SubmitInfo submitInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = submitInfoDao.insertSelective(submitInfo);
        res.put("status", result);
        res.put("submId", submitInfo.getSubmId());
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(SubmitInfo submitInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = submitInfoDao.updateByPrimaryKeySelective(submitInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = submitInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/testData", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> testData(SubmitInfo submitInfo) {
        Map<String, Object> res = new HashMap<>();
        submitInfo.setUserId(getUserId());
        submitInfo.setIp(getIp(request));
        submitInfo.setIsTeacherTest(1);
        submitInfo.setStatus(11);
        int result = submitInfoDao.insertSelective(submitInfo);
        res.put("status", result);
        res.put("submId", submitInfo.getSubmId());
        if (submitInfo.getSubmId() != null) {
            ApplicationStartListener.queue.add(submitInfo.getSubmId());
        }
        return res;
    }

    @RequestMapping(value = "/rejudgeBySubmId", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> rejudgeBySubmId(
            @RequestParam(required = false) Integer submId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer probId) {
        Map<String, Object> res = new HashMap<>();
        if (submId != null) {
            ApplicationStartListener.queue.add(submId);
        } else if (status != null || probId != null) {
            asynRejudge(status, probId);
        }
        res.put("status", 1);
        return res;
    }

    private void asynRejudge(Integer status, Integer probId) {
        RejudgeThread rejudgeThread = new RejudgeThread();
        if (status == 10 || status == 11 || status == 12) rejudgeThread.setStatus(status);
        rejudgeThread.setProbId(probId);
        ApplicationStartListener.rejudgeExecutor.execute(rejudgeThread);
    }

    private final class RejudgeThread implements Runnable {
        private Integer status;
        private Integer probId;

        public RejudgeThread() {
        }

        public RejudgeThread(Integer status, Integer probId) {
            this.status = status;
            this.probId = probId;
        }

        @Override
        public void run() {
            SubmitInfo condition = new SubmitInfo();
            condition.setStatus(status);
            condition.setProblemId(probId);
            int total = submitInfoDao.selectByConditionGetCount(condition, new BaseQuery());
            if (total == 0) return;
            // 开始重判
            Integer rejudgeSingleNum = Config.getToInt(SiteKey.REJUDGE_SINGLE_NUM, SiteKey.REJUDGE_SINGLE_NUM_DE);
            int len = total % rejudgeSingleNum != 0 ?
                    total / rejudgeSingleNum + 1 : total / rejudgeSingleNum;
            BaseQuery baseQuery = new BaseQuery();
            baseQuery.setRows(rejudgeSingleNum);
            SubmitInfo record = new SubmitInfo();
            for (int i = 0; i < len; i++) {
                baseQuery.setPage(i + 1);
                List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(condition, baseQuery);
                if (submitInfoList == null || submitInfoList.size() == 0) return;
                for (SubmitInfo item : submitInfoList) {
                    if (item == null || item.getSubmId() == null) continue;
                    if (ApplicationStartListener.queue.contains(item.getSubmId())) continue;
                    record.setSubmId(item.getSubmId());
                    record.setStatus(13);// 更新为Rejudge.Waiting
                    submitInfoDao.updateByPrimaryKeySelective(record);
                    ApplicationStartListener.queue.add(item.getSubmId());
                }
                try {
                    Integer rejudgeWaitTime = Config.getToInt(SiteKey.REJUDGE_WAIT_TIME, SiteKey.REJUDGE_WAIT_TIME_DE);
                    Thread.sleep(rejudgeWaitTime);
                } catch (InterruptedException e) {
                    log.error("Thread.sleep error: {}", e);
                }
            }
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getProbId() {
            return probId;
        }

        public void setProbId(Integer probId) {
            this.probId = probId;
        }
    }
}
