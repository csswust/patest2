package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.result.SelectProblemNumRe;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.sim.SimInput;
import com.csswust.patest2.service.sim.SimOutput;
import com.csswust.patest2.service.sim.SimResult;
import com.csswust.patest2.service.sim.SimService;
import com.csswust.patest2.utils.SimHash;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.dc.path.PathError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/24.
 */
@Validated
@RestController
@RequestMapping("/submitSimilarity")
public class SubmitSimilarityAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(SubmitSimilarityAction.class);

    @Autowired
    private SubmitSimilarityDao submitSimilarityDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private SimService simService;
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private PaperProblemDao paperProblemDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            SubmitSimilarity submitSimilarity,
            @RequestParam(required = false) Double lowerLimit,
            @RequestParam(required = false) Double upperLimit,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setCustom("lowerLimit", lowerLimit);
        baseQuery.setCustom("upperLimit", upperLimit);
        if (StringUtils.isNotBlank(userName)) {
            UserInfo userInfo = userInfoDao.selectByUsername(userName);
            if (userInfo != null) {
                SubmitInfo submitInfo = new SubmitInfo();
                submitInfo.setUserId(userInfo.getUserId());
                List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(submitInfo, new BaseQuery());
                List<Integer> submId = getFieldByList(submitInfoList, "submId", SubmitInfo.class);
                baseQuery.setCustom("submitIds1", submId);
            }
        }
        Integer total = submitSimilarityDao.selectByConditionGetCount(submitSimilarity, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<SubmitSimilarity> submitSimilarityList = submitSimilarityDao.selectByCondition(submitSimilarity, baseQuery);
        List<ExamInfo> examInfoList = selectRecordByIds(
                getFieldByList(submitSimilarityList, "examId", SubmitSimilarity.class),
                "examId", (BaseDao) examInfoDao, ExamInfo.class);
        List<ProblemInfo> problemInfoList = selectRecordByIds(
                getFieldByList(submitSimilarityList, "problemId", SubmitSimilarity.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        List<SubmitInfo> submitInfoList1 = selectRecordByIds(
                getFieldByList(submitSimilarityList, "submitId1", SubmitSimilarity.class),
                "submId", (BaseDao) submitInfoDao, SubmitInfo.class);
        List<UserInfo> userInfoList1 = selectRecordByIds(
                getFieldByList(submitInfoList1, "userId", SubmitInfo.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList1 = selectRecordByIds(
                getFieldByList(userInfoList1, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);

        List<SubmitInfo> submitInfoList2 = selectRecordByIds(
                getFieldByList(submitSimilarityList, "submitId2", SubmitSimilarity.class),
                "submId", (BaseDao) submitInfoDao, SubmitInfo.class);
        List<UserInfo> userInfoList2 = selectRecordByIds(
                getFieldByList(submitInfoList2, "userId", SubmitInfo.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList2 = selectRecordByIds(
                getFieldByList(userInfoList2, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        res.put("total", total);
        res.put("examInfoList", examInfoList);
        res.put("problemInfoList", problemInfoList);

        res.put("submitSimilarityList", submitSimilarityList);
        res.put("submitInfoList1", submitInfoList1);
        res.put("submitInfoList2", submitInfoList2);
        res.put("userInfoList1", userInfoList1);
        res.put("userProfileList1", userProfileList1);
        res.put("userInfoList2", userInfoList2);
        res.put("userProfileList2", userProfileList2);
        return res;
    }

    private final class SimThread implements Runnable {
        private Integer examId;

        @Override
        public void run() {
            APIResult apiResult = new APIResult();
            apiResult.setStatusAndDesc(0, "运行中");
            apiResult.setDataKey("schedule", 0.0);
            simMap.put(examId, apiResult);
            ExamInfo record = new ExamInfo();
            record.setExamId(examId);
            record.setIsSimTest(1);
            examInfoDao.updateByPrimaryKeySelective(record);
            List<SelectProblemNumRe> problemNumReList = paperProblemDao.selectProblemNum(examId);
            if (problemNumReList == null || problemNumReList.size() == 0) {
                apiResult.setStatusAndDesc(-2, "无题目数据");
                return;
            }
            int sum = 0;
            for (SelectProblemNumRe problemNumRe : problemNumReList) {
                sum += problemNumRe.getNum();
            }
            int count = 0, tempSum = 0;
            for (SelectProblemNumRe problemNumRe : problemNumReList) {
                if (problemNumRe == null) continue;
                if (problemNumRe.getProbId() == null) continue;
                PaperProblem condition = new PaperProblem();
                condition.setExamId(examId);
                condition.setProblemId(problemNumRe.getProbId());
                List<PaperProblem> paperProblemList = paperProblemDao.selectByCondition(condition, new BaseQuery());
                if (paperProblemList == null || paperProblemList.size() == 0) continue;

                Map<Integer, List<SubmitInfo>> map = new HashMap<>();
                List<SubmitInfo> submitInfoList = selectRecordByIds(
                        getFieldByList(paperProblemList, "submitId", PaperProblem.class),
                        "submId", (BaseDao) submitInfoDao, SubmitInfo.class);
                for (SubmitInfo submitInfo : submitInfoList) {
                    if (submitInfo == null) continue;
                    if (submitInfo.getSubmId() == null) continue;
                    if (submitInfo.getUserId() == null) continue;
                    if (submitInfo.getJudgerId() == null) continue;
                    Integer judgeId = submitInfo.getJudgerId();
                    List<SubmitInfo> temp = map.get(judgeId);
                    if (temp == null) temp = new ArrayList<>();
                    map.put(judgeId, temp);
                    temp.add(submitInfo);
                }
                for (Map.Entry<Integer, List<SubmitInfo>> entry : map.entrySet()) {
                    Integer judgeId = entry.getKey();
                    List<SubmitInfo> temp = entry.getValue();

                    if (temp == null || temp.size() == 0) continue;
                    SimInput simInput = new SimInput();
                    List<SubmitInfo> leftList = new ArrayList<>();
                    List<SubmitInfo> rightList = new ArrayList<>();
                    for (SubmitInfo submitInfo : temp) {
                        leftList.add(submitInfo);
                        rightList.add(submitInfo);
                    }
                    simInput.setLeftCodeList(leftList);
                    simInput.setRightCodeList(rightList);
                    simInput.setScriptPath(getScriptPathByJudgeId(judgeId));
                    SimOutput simOutput = simService.judge(simInput);
                    if (simOutput.getError() != null) continue;
                    List<SimResult> simResultList = simOutput.getSimResultList();
                    List<SubmitSimilarity> similarityList = dataConsert(simResultList,
                            examId, problemNumRe.getProbId());
                    int insertCount = submitSimilarityDao.insertBatch(similarityList);
                    count = count + insertCount;
                }
                tempSum += problemNumRe.getNum();
                apiResult.setDataKey("schedule", tempSum * 1.0 / sum);
            }
            if (count != 0) {
                apiResult.setStatusAndDesc(count, "共检测出" + count + "条相似度");
            } else {
                apiResult.setStatusAndDesc(-1, "无记录");
            }
        }

        public SimThread(Integer examId) {
            this.examId = examId;
        }
    }

    private final static Map<Integer, APIResult> simMap = new ConcurrentHashMap<>();
    private final static ExecutorService simExecutor = Executors.newFixedThreadPool(1);

    @RequestMapping(value = "/getSimByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getSimByExamId(@RequestParam Integer examId) {
        APIResult apiResult = new APIResult();
        if (examId == null) {
            apiResult.setStatusAndDesc(-1, "examId不能为空");
            return apiResult;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            apiResult.setStatusAndDesc(-1, "examInfo为空");
            return apiResult;
        }
        if (examInfo.getIsSimTest() != 0) {
            apiResult.setStatusAndDesc(-1, "不能重复计算相似度");
            return apiResult;
        }
        SimThread simThread = new SimThread(examId);
        simExecutor.execute(simThread);
        apiResult.setStatusAndDesc(1, "检测任务提交成功");
        return apiResult;
    }

    @RequestMapping(value = "/getSimStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getSimStatus(@RequestParam Integer examId) {
        APIResult apiResult = new APIResult();
        if (examId == null) {
            apiResult.setStatusAndDesc(-1, "examId不能为空");
            return apiResult;
        }
        APIResult temp = simMap.get(examId);
        if (temp == null) {
            apiResult.setStatusAndDesc(-1, "状态为空");
        } else {
            Double schedule = (Double) temp.getData().get("schedule");
            apiResult.setDataKey("schedule", String.valueOf((int) (schedule * 100)) + "%");
            apiResult.setStatusAndDesc(temp.getStatus(), temp.getDesc());
        }
        return apiResult;
    }

    private String getScriptPathByJudgeId(Integer judgeId) {
        String scriptPath = Config.get(SiteKey.SIM_SCRIPT_PATH, SiteKey.SIM_SCRIPT_PATH_DE);
        if (judgeId == 1) scriptPath = scriptPath + "/sim_c";
        else if (judgeId == 2) scriptPath = scriptPath + "/sim_c++";
        else if (judgeId == 3) scriptPath = scriptPath + "/sim_java";
        else scriptPath = scriptPath + "/sim_text";
        return scriptPath;
    }

    private List<SubmitSimilarity> dataConsert(List<SimResult> simResultList, Integer examId, Integer problemId) {
        List<SubmitSimilarity> similarityList = new ArrayList<>();
        if (simResultList == null || simResultList.size() == 0) return similarityList;
        for (int i = 0; i < simResultList.size(); i++) {
            SimResult simResult = simResultList.get(i);
            SubmitSimilarity similarity = new SubmitSimilarity();
            if (simResult.getSubmId1().intValue() == simResult.getSubmId2().intValue()) continue;
            similarity.setSubmitId1(simResult.getSubmId1());
            similarity.setSubmitId2(simResult.getSubmId2());
            similarity.setExamId(examId);
            similarity.setSimilarity(simResult.getValue());
            similarity.setProblemId(problemId);
            similarityList.add(similarity);
        }
        return similarityList;
    }

    @RequestMapping(value = "/getSimBySubmId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getSimBySubmId(
            @RequestParam Integer examId,
            @RequestParam Integer submId) {
        APIResult apiResult = new APIResult();
        if (examId == null || submId == null) return apiResult;
        SubmitInfo currSubmitInfo = submitInfoDao.selectByPrimaryKey(submId);
        if (currSubmitInfo == null) return apiResult;
        if (currSubmitInfo.getProblemId() == null) return apiResult;
        if (currSubmitInfo.getJudgerId() == null) return apiResult;
        if (currSubmitInfo.getUserId() == null) return apiResult;
        Integer judgerId = currSubmitInfo.getJudgerId();

        // 判断该提交是否已经计算过相似度
        SubmitSimilarity temp = new SubmitSimilarity();
        temp.setSubmitId1(submId);
        int total = submitSimilarityDao.selectByConditionGetCount(temp, new BaseQuery());
        if (total > 0) {
            apiResult.setStatusAndDesc(-500, "不能重复计算相似度");
            return apiResult;
        }
        // 查询属于同一个题目下,同一种语言的所有提交
        SubmitInfo submitInfo = new SubmitInfo();
        submitInfo.setExamId(examId);
        submitInfo.setProblemId(currSubmitInfo.getProblemId());
        submitInfo.setJudgerId(judgerId);
        int testMaxNum = Config.getToInt(SiteKey.SIM_TEST_DATA_MAX_NUM,
                SiteKey.SIM_TEST_DATA_MAX_NUM_DE);
        List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(submitInfo,
                new BaseQuery(1, testMaxNum));
        if (submitInfoList == null || submitInfoList.size() == 0) {
            apiResult.setStatusAndDesc(-1, "本提交无提交数据");
            return apiResult;
        }
        SimInput simInput = new SimInput();
        List<SubmitInfo> leftList = new ArrayList<>();
        List<SubmitInfo> rightList = new ArrayList<>();
        leftList.add(currSubmitInfo);

        // 计算相似度
        int count = 0, length = submitInfoList.size();
        for (int i = 0; i < length; i++) {
            SubmitInfo subInfo2 = submitInfoList.get(i);
            // 同一个人submId
            if (subInfo2 == null) continue;
            if (subInfo2.getSubmId() == null) continue;
            if (subInfo2.getUserId() == null) continue;
            if (submId.intValue() == subInfo2.getSubmId().intValue()) continue;
            if (currSubmitInfo.getUserId().intValue() == subInfo2.getUserId().intValue()) continue;
            rightList.add(subInfo2);
        }
        if (rightList.size() == 0) {
            apiResult.setStatusAndDesc(-2, "本提交无参考数据");
            return apiResult;
        }
        simInput.setLeftCodeList(leftList);
        simInput.setRightCodeList(rightList);
        String scriptPath = getScriptPathByJudgeId(judgerId);
        simInput.setScriptPath(scriptPath);
        SimOutput simOutput = simService.judge(simInput);
        if (simOutput.getError() != null) {
            apiResult.setStatusAndDesc(-3, simOutput.getError());
            return apiResult;
        }
        List<SimResult> simResultList = simOutput.getSimResultList();
        if (simResultList == null || simResultList.size() == 0) {
            apiResult.setStatusAndDesc(-4, "相识度计算无结果");
            return apiResult;
        }
        int saveMaxNum = Config.getToInt(SiteKey.SIM_SAVE_DATABASE_NUM,
                SiteKey.SIM_SAVE_DATABASE_NUM_DE);
        List<SubmitSimilarity> similarityList = new ArrayList<>();
        for (int i = 0; i < simResultList.size() && i < saveMaxNum; i++) {
            SimResult simResult = simResultList.get(i);
            SubmitSimilarity similarity = new SubmitSimilarity();
            similarity.setSubmitId1(simResult.getSubmId1());
            similarity.setSubmitId2(simResult.getSubmId2());
            similarity.setExamId(examId);
            similarity.setSimilarity(simResult.getValue());
            similarity.setProblemId(currSubmitInfo.getProblemId());
            similarityList.add(similarity);
        }
        int insertCount = submitSimilarityDao.insertBatch(similarityList);
        if (insertCount != 0) {
            apiResult.setStatusAndDesc(1, "成功");
        } else {
            apiResult.setStatusAndDesc(-5, "插入失败");
        }
        return apiResult;
    }

    private void diff(Integer submId1, Integer submId2, double value) {
        SubmitInfo submitInfo1 = submitInfoDao.selectByPrimaryKey(submId1);
        SubmitInfo submitInfo2 = submitInfoDao.selectByPrimaryKey(submId2);
        Double oldValue = SimHash.getSimilarity(submitInfo1.getSource(), submitInfo2.getSource());
        System.out.printf("%d %d %f %f\n", submId1, submId2, value, oldValue);
    }
}
