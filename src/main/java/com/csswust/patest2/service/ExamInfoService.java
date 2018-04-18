package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.service.result.ImportDataRe;

/**
 * Created by 972536780 on 2018/3/23.
 */
public interface ExamInfoService {
    ImportDataRe importCodeByExamId(Integer examId);

    ImportDataRe importGradeByExamId(Integer examId);

    APIResult selectById(Integer examId);

    APIResult updateById(ExamInfo examInfo);
}
