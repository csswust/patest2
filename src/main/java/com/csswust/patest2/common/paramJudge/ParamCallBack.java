package com.csswust.patest2.common.paramJudge;

/**
 * Created by 972536780 on 2018/3/15.
 */
public interface ParamCallBack<T> {
    boolean judgeType(Object object);

    T replaceParam(T t);
}
