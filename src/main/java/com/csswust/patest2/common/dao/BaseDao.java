package com.csswust.patest2.common.dao;

import java.util.List;

/**
 * Created by 972536780 on 2017/11/26.
 */
public interface BaseDao<T, Q> {
    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKeyWithBLOBs(T record);

    int updateByPrimaryKey(T record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<T> selectByCondition(T record, Q query);

    int selectByConditionGetCount(T record, Q query);

    List<T> selectByIds(String ids);

    List<T> selectByIdsList(List<Integer> idsList);
}
