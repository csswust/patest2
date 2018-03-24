package com.csswust.patest2.service;

import com.csswust.patest2.service.result.OnlineListRe;

/**
 * Created by 972536780 on 2018/3/22.
 */
public interface OnlineUserService {
    OnlineListRe getOnlineList(Integer page, Integer rows);
}
