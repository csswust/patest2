package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.vo.PersonExamPaper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 972536780 on 2018/3/21.
 */
public interface ExamPaperService {
    APIResult selectByCondition(ExamPaper examPaper, Boolean onlyPaper,
                                Boolean containOnline, String userName,
                                String studentNumber, Integer page, Integer rows);

    APIResult insertByExcel(MultipartFile file, Integer examId, boolean isIgnoreError);

    APIResult drawProblemByExamId(Integer examId, Integer userId);

    APIResult insertOne(Integer examId, String studentNumber,String password);

    File exportInfoByExamId(Integer examId) throws IOException;

    List<PersonExamPaper> getClassPaperScore(Integer examId);
}
