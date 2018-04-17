package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.EpOrderInfo;

/**
 * Created by 972536780 on 2018/4/17.
 */
public interface EpOrderInfoService {
    APIResult selectByCondition(EpOrderInfo epOrderInfo, Integer page, Integer rows);
}
