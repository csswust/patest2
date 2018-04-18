package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.ExamInfoService;
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
@RequestMapping("/ep/examInfo")
public class EpExamInfoAction extends BaseAction {
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private AuthService authService;

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
