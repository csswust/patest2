package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.service.result.ImportDataRe;
import com.csswust.patest2.service.result.SelectProblemDataRe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/19.
 */
public interface ProblemInfoService {
    APIResult selectByCondition(ProblemInfo problemInfo, Boolean containUModify,
                                Boolean containCode,
                                Integer page, Integer rows);

    APIResult insertProblemData(Integer probId, List<String> input, List<String> output);

    APIResult insertProblemData(Integer probId, MultipartFile zipFile);

    SelectProblemDataRe selectProblemData(Integer probId);

    ImportDataRe importProblmData(Integer probId);
}
