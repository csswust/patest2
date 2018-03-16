package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.VisitPath;

import java.util.List;

public interface VisitPathDao {
    int deleteByPrimaryKey(Integer visPatId);

    int insert(VisitPath record);

    int insertSelective(VisitPath record);

    VisitPath selectByPrimaryKey(Integer visPatId);

    int updateByPrimaryKeySelective(VisitPath record);

    int updateByPrimaryKey(VisitPath record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<VisitPath> selectByCondition(VisitPath record, BaseQuery query);

    int selectByConditionGetCount(VisitPath record, BaseQuery query);
}