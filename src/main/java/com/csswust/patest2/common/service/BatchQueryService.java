package com.csswust.patest2.common.service;

import com.csswust.patest2.common.dao.BaseDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/14.
 */
public class BatchQueryService {
    /**
     * 通过list和字段名，反射获取主键列表
     *
     * @param paramLsit list
     * @param fieldName 字段名
     * @param <C>       参数list类型
     * @return ids
     */
    public static <C> List<Integer> getFieldByList(List<C> paramLsit, String fieldName) {
        List<Integer> fieldList = new ArrayList<>();
        if (paramLsit == null || paramLsit.size() == 0) {
            return fieldList;
        }
        Class<?> clz = paramLsit.get(0).getClass();
        Field field = null;
        try {
            field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return fieldList;
        }
        for (C item : paramLsit) {
            try {
                Integer value = (Integer) field.get(item);
                fieldList.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldList.add(null);
            }
        }
        return fieldList;
    }

    /**
     * 通过ids查询列表，注意结果不一定和和ids一致，比如ids为1,-1,2
     *
     * @param paramLsit ids
     * @param fieldName 字段名
     * @param baseDao   对应处理的dao
     * @param <T>       返回结果类似
     * @return 返回结果
     */
    public static <T> List<T> selectRecordByIds(List<Integer> paramLsit, String fieldName, BaseDao baseDao) {
        Map<Integer, T> map = new HashMap<>();
        List<T> temp = baseDao.selectByIdsList(paramLsit);
        List<T> resultList = new ArrayList<>();
        for (int i = 0; i < paramLsit.size(); i++) {
            resultList.add(null);
        }
        if (temp == null || temp.size() == 0) {
            return resultList;
        }
        Class<?> clz = temp.get(0).getClass();
        Field field = null;
        try {
            field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return resultList;
        }
        for (T item : temp) {
            try {
                Integer value = (Integer) field.get(item);
                map.put(value, item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < paramLsit.size(); i++) {
            if (paramLsit.get(i) == null) {
                resultList.set(i, null);
            } else {
                resultList.set(i, map.get(paramLsit.get(i)));
            }
        }
        return resultList;
    }
}
