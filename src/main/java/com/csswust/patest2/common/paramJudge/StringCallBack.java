package com.csswust.patest2.common.paramJudge;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by 972536780 on 2018/3/15.
 */
public class StringCallBack implements ParamCallBack<String> {
    @Override
    public boolean judgeType(Object object) {
        return object instanceof String;
    }

    @Override
    public String replaceParam(String s) {
        if (s == null) return null;
        s = s.trim();
        if (StringUtils.isBlank(s)) {
            return null;
        } else {
            return s;
        }
    }
}
