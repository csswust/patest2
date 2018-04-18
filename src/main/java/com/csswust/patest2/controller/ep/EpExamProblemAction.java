package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamProblemDao;
import com.csswust.patest2.entity.ExamProblem;
import com.csswust.patest2.service.ExamProblemService;
import com.csswust.patest2.service.common.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/4/18.
 */
@RestController
@RequestMapping("/ep/examProblem")
public class EpExamProblemAction extends BaseAction {
    @Autowired
    private ExamProblemService examProblemService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ExamProblemDao examProblemDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            @RequestParam Integer examId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examProblemService.selectByCondition(examId, page, rows);
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertByArray(
            @RequestParam Integer examId,
            @RequestParam Integer[] probIdList) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examProblemService.insertByArray(examId, probIdList);
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        return examProblemService.deleteByIds(ids);
    }

    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteById(@RequestParam Integer id) {
        ExamProblem examProblem = examProblemDao.selectByPrimaryKey(id);
        if (examProblem == null) return null;
        if (!authService.judgeEpAuth(getEpUserId(), examProblem.getExamId())) {
            return new APIResult(-501, "权限不足");
        }
        return examProblemService.deleteById(id);
    }
}
