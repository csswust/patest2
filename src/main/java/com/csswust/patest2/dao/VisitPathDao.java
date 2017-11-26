package com.csswust.patest2.dao;

import com.csswust.patest2.entity.VisitPath;

public interface VisitPathDao {
    int deleteByPrimaryKey(Integer visPatId);

    int insert(VisitPath record);

    int insertSelective(VisitPath record);

    VisitPath selectByPrimaryKey(Integer visPatId);

    int updateByPrimaryKeySelective(VisitPath record);

    int updateByPrimaryKey(VisitPath record);
}