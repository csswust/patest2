package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.ExamParam;

/**
 * Created by 972536780 on 2018/3/20.
 */
public interface ExamParamService {
    APIResult selectByCondition(ExamParam examParam, Integer page, Integer rows);

    APIResult selectProblemTotal(Integer knowId, Integer levelId, Integer examId);

    APIResult insertByArray(Integer examId, Integer[] knowIds, Integer[] levels, Integer[] scores);

    APIResult deleteByIds(String ids);

    APIResult deleteById(Integer id);
}
