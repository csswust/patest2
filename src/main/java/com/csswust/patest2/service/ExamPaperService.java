package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.service.result.DrawProblemRe;
import com.csswust.patest2.service.result.ExamPaperLoadRe;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 972536780 on 2018/3/21.
 */
public interface ExamPaperService {
    APIResult selectByCondition(ExamPaper examPaper, Boolean onlyPaper,
                                Boolean containOnline, String userName,
                                String studentNumber, Integer page, Integer rows);

    APIResult insertByExcel(MultipartFile file, Integer examId, boolean isIgnoreError);

    APIResult drawProblemByExamId(Integer examId, Integer userId);
}
