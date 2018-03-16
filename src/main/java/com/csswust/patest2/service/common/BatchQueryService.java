package com.csswust.patest2.service.common;

import com.csswust.patest2.dao.common.BaseDao;

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
    public static <C> List<Integer> getFieldByList(List<C> paramLsit, String fieldName, Class<C> clz) {
        List<Integer> fieldList = new ArrayList<>();
        if (paramLsit == null || paramLsit.size() == 0) {
            return fieldList;
        }
        for (int i = 0; i < paramLsit.size(); i++) {
            fieldList.add(null);
        }
        Field field = null;
        try {
            field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return fieldList;
        }
        for (int i = 0; i < paramLsit.size(); i++) {
            C item = paramLsit.get(i);
            if (item == null) {
                fieldList.set(i, null);
                continue;
            }
            try {
                Integer value = (Integer) field.get(item);
                fieldList.set(i, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldList.set(i, null);
            }
        }
        return fieldList;
    }

    public static <T> List<T> selectRecordByIds(List<Integer> paramLsit, String fieldName, BaseDao baseDao) {
        return selectRecordByIds(paramLsit, fieldName, baseDao, null);
    }

    /**
     * 通过ids查询列表，注意结果不一定和和ids一致，比如ids为1,-1,2
     *
     * @param paramLsit ids
     * @param fieldName 字段名
     * @param baseDao   对应处理的dao
     * @param <T>       返回结果类似
     * @param clz       如果结果为空，那么有t填充
     * @return 返回结果
     */
    public static <T> List<T> selectRecordByIds(List<Integer> paramLsit, String fieldName, BaseDao baseDao, Class<T> clz) {
        Map<Integer, T> map = new HashMap<>();
        List<T> temp = baseDao.selectByIdsList(paramLsit);
        List<T> resultList = new ArrayList<>();
        Field field = null;
        T replace = null;
        try {
            replace = clz.newInstance();
            field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (Exception e) {
            return resultList;
        }
        for (int i = 0; i < paramLsit.size(); i++) {
            resultList.add(replace);
        }
        if (temp == null || temp.size() == 0) {
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
                resultList.set(i, replace);
            } else {
                T reulut = map.get(paramLsit.get(i));
                resultList.set(i, reulut == null ? replace : reulut);
            }
        }
        return resultList;
    }
}
