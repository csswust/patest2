package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.VisitPathDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.VisitPath;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("visitPathDao")
public class VisitPathDaoImpl extends CommonMapper<VisitPath, BaseQuery> implements VisitPathDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.VisitPathDao.";
    }

    @Override
    public void insertInit(VisitPath record, Date date) {
        record.setVisPatId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(VisitPath record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }
}
