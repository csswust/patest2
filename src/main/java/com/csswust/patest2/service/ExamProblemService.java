package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;

/**
 * Created by 972536780 on 2018/4/18.
 */
public interface ExamProblemService {
    APIResult selectByCondition(Integer examId, Integer page, Integer rows);

    APIResult insertByArray(Integer examId, Integer[] probIdList);

    APIResult deleteByIds(String ids);
}
