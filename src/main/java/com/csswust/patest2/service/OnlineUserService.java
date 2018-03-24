package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.service.result.OnlineListRe;

import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/22.
 */
public interface  OnlineUserService {
    OnlineListRe getOnlineList(Integer page, Integer rows);
}
