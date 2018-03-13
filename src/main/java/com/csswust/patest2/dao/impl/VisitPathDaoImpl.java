package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.VisitPathDao;
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
    public void insertInit(VisitPath record) {
        record.setVisPatId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(VisitPath record) {
        record.setModifyTime(new Date());
    }
}
