package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.service.ExamParamService;
import com.csswust.patest2.service.common.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/ep/examParam")
public class EpExamParamAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(EpExamParamAction.class);

    @Autowired
    private ExamParamService examParamService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ExamParamDao examParamDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            @RequestParam Integer examId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);
        return examParamService.selectByCondition(examParam, page, rows);
    }

    @RequestMapping(value = "/selectProblemTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectProblemTotal(
            @RequestParam(required = false) Integer knowId,
            @RequestParam(required = false) Integer levelId,
            @RequestParam Integer examId) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examParamService.selectProblemTotal(knowId, levelId, examId);
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertByArray(
            @RequestParam Integer examId,
            @RequestParam Integer[] knowIds,
            @RequestParam Integer[] levels,
            @RequestParam Integer[] scores) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examParamService.insertByArray(examId, knowIds, levels, scores);
    }

    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteById(@RequestParam Integer id) {
        ExamParam examParam = examParamDao.selectByPrimaryKey(id);
        if (examParam == null) return null;
        if (!authService.judgeEpAuth(getEpUserId(), examParam.getExamId())) {
            return new APIResult(-501, "权限不足");
        }
        return examParamService.deleteById(id);
    }
}