package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.common.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/21.
 */
@RestController
@RequestMapping("/ep/examPaper")
public class EpExamPaperAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(EpExamPaperAction.class);

    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            @RequestParam Integer examId,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyPaper,
            @RequestParam(required = false, defaultValue = "false") Boolean containOnline,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        ExamPaper examPaper = new ExamPaper();
        examPaper.setExamId(examId);
        return examPaperService.selectByCondition(examPaper, onlyPaper, containOnline,
                userName, studentNumber, page, rows);
    }

    @RequestMapping(value = "/uploadUserByExamId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object uploadUserByExamId(
            @RequestParam Integer examId,
            @RequestParam MultipartFile namefile,
            @RequestParam(required = false, defaultValue = "false") Boolean isIgnoreError) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        if (namefile == null) return new APIResult(-501, "namefile不能为空");
        return examPaperService.insertByExcel(namefile, examId, isIgnoreError);
    }

    @RequestMapping(value = "/drawProblem", method = {RequestMethod.GET, RequestMethod.POST})
    public Object drawProblem(@RequestParam Integer examId) {
        if (!authService.judgeEpAuth(getEpUserId(), examId)) {
            return new APIResult(-501, "权限不足");
        }
        return examPaperService.drawProblemByExamId(examId, null);
    }
}
