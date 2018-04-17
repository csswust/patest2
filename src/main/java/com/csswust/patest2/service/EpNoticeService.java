package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.EpNotice;

/**
 * Created by 972536780 on 2018/4/17.
 */
public interface EpNoticeService {
    APIResult selectByCondition(EpNotice epNotice, Integer page, Integer rows);
}
