package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.UserInfo;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/28.
 */
public interface EpService {
    APIResult selectUserInfoList(List<UserInfo> userInfoList);
}
