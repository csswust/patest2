package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.VisitPath;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.VisitPathDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class VisitPathDaoImpl extends CommonMapper implements VisitPathDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.VisitPathDao.";

    @Override
    public int deleteByPrimaryKey(Integer visPatId) {
        if (visPatId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", visPatId);
    }

    @Override
    public int insert(VisitPath record) {
        if (record == null) {
            return 0;
        }
        record.setVisPatId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(VisitPath record) {
        if (record == null) {
            return 0;
        }
        record.setVisPatId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public VisitPath selectByPrimaryKey(Integer visPatId) {
        if (visPatId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", visPatId);
    }

    @Override
    public int updateByPrimaryKeySelective(VisitPath record) {
        if (record == null || record.getVisPatId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(VisitPath record) {
        if (record == null || record.getVisPatId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
