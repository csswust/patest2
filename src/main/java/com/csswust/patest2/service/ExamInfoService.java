package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.result.ImportDataRe;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/23.
 */
public interface ExamInfoService {
    APIResult selectMyProblem(Integer examId);

    APIResult rankingByGrade(Integer examId, String userName, String studentNumber,
                             Integer page, Integer rows);

    APIResult importCodeByExamId(Integer examId);

    APIResult importGradeByExamId(Integer examId);

    APIResult selectById(Integer examId);

    APIResult updateById(ExamInfo examInfo);

    APIResult buildExamState(APIResult apiResult, List<ExamInfo> examInfoList, boolean containUModify);
}
