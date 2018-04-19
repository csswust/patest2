package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.ExamInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.ExamInfoService;
import com.csswust.patest2.service.common.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/18.
 */
@RestController
@RequestMapping("/ep/examInfo")
public class EpExamInfoAction extends BaseAction {
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EpApplyInfoDao epApplyInfoDao;
    @Autowired
    private ExamInfoDao examInfoDao;

    @RequestMapping(value = "/selectMyExam", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectMyExam(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        APIResult apiResult = new APIResult();
        EpApplyInfo epApplyInfo = new EpApplyInfo();
        epApplyInfo.setEpUserId(getEpUserId());
        epApplyInfo.setStatus(2);
        int total = epApplyInfoDao.selectByConditionGetCount(epApplyInfo, new BaseQuery());
        List<EpApplyInfo> epApplyInfoList = epApplyInfoDao.selectByCondition(
                epApplyInfo, new BaseQuery(page, rows));
        List<ExamInfo> examInfoList = selectRecordByIds(
                getFieldByList(epApplyInfoList, "examId", EpApplyInfo.class),
                "examId", (BaseDao) examInfoDao, ExamInfo.class);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("examInfoList", examInfoList);
        examInfoService.buildExamState(apiResult, examInfoList, false);
        return apiResult;
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectById(@RequestParam Integer examId) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examInfoService.selectById(examId);
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(ExamInfo examInfo) {
        if (examInfo == null) return null;
        if (!authService.judgeEpAuth(getEpUserId(), examInfo.getExamId())) {
            return new APIResult(-501, "权限不足");
        }
        return examInfoService.updateById(examInfo);
    }
}
