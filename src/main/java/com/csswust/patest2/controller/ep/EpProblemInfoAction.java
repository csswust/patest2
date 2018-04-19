package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.service.ProblemInfoService;
import com.csswust.patest2.service.common.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/ep/problemInfo")
public class EpProblemInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(EpProblemInfoAction.class);

    @Autowired
    private ProblemInfoService problemInfoService;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            ProblemInfo problemInfo,
            @RequestParam Integer examId,
            @RequestParam(required = false, defaultValue = "false") Boolean containUModify,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return problemInfoService.selectByCondition(problemInfo, containUModify,
                false, page, rows);
    }
}
