package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.service.ExamProblemService;
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
@RequestMapping("/examProblem")
public class ExamProblemAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamProblemAction.class);

    @Autowired
    private ExamProblemService examProblemService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            @RequestParam Integer examId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return examProblemService.selectByCondition(examId, page, rows);
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertByArray(
            @RequestParam Integer examId,
            @RequestParam Integer[] probIdList) {
        return examProblemService.insertByArray(examId, probIdList);
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        return examProblemService.deleteByIds(ids);
    }
}
