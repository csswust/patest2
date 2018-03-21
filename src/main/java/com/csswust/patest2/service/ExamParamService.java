package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;

/**
 * Created by 972536780 on 2018/3/20.
 */
public interface ExamParamService {
    APIResult insertByArray(Integer examId, Integer[] knowIds, Integer[] levels, Integer[] scores);
}
