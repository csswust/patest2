package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.EpApplyInfo;

/**
 * Created by 972536780 on 2018/4/17.
 */
public interface EpApplyInfoService {
    APIResult selectByCondition(EpApplyInfo epApplyInfo, Integer page, Integer rows);

    APIResult accept(Integer applyId, Integer status, Double money, String reason);

    APIResult deleteById(Integer applyId);
}
