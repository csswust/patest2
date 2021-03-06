package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.entity.ExamParam;
import com.csswust.patest2.service.ExamParamService;
import com.csswust.patest2.service.OperateLogService;
import com.csswust.patest2.service.input.OperateLogInsert;
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
@RequestMapping("/examParam")
public class ExamParamAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamParamAction.class);

    @Autowired
    private ExamParamService examParamService;
    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            ExamParam examParam,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examParam == null) return new APIResult(-501, "examParam不能为空");
        return examParamService.selectByCondition(examParam, page, rows);
    }

    @RequestMapping(value = "/selectProblemTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectProblemTotal(
            @RequestParam(required = false) Integer knowId,
            @RequestParam(required = false) Integer levelId,
            @RequestParam Integer examId) {
        if (examId == null) new APIResult(-501, "examId不能为空");
        return examParamService.selectProblemTotal(knowId, levelId, examId);
    }

    @RequestMapping(value = "/insertByArray", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertByArray(
            @RequestParam Integer examId,
            @RequestParam Integer[] knowIds,
            @RequestParam Integer[] levels,
            @RequestParam Integer[] scores) {
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "编辑考试模板", examId);
        insert.setArgcData("knowIds", knowIds);
        insert.setArgcData("levels", levels);
        insert.setArgcData("scores", scores);
        operateLogService.insertOne(insert);
        if (examId == null) return new APIResult(-501, "examId不能为空");

        return examParamService.insertByArray(examId, knowIds, levels, scores);
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "删除考试模板");
        insert.setArgcData("ids", ids);
        operateLogService.insertOne(insert);
        return examParamService.deleteByIds(ids);
    }

    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteById(@RequestParam Integer id) {
        OperateLogInsert insert = new OperateLogInsert(getUserId(), getIp(),
                getUrl(), "删除考试模板");
        insert.setArgcData("id", id);
        return examParamService.deleteById(id);
    }
}