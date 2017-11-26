package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.LevelType;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.LevelTypeDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class LevelTypeDaoImpl extends CommonMapper implements LevelTypeDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.LevelTypeDao.";

    @Override
    public int deleteByPrimaryKey(Integer levTypId) {
        if (levTypId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", levTypId);
    }

    @Override
    public int insert(LevelType record) {
        if (record == null) {
            return 0;
        }
        record.setLevTypId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(LevelType record) {
        if (record == null) {
            return 0;
        }
        record.setLevTypId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public LevelType selectByPrimaryKey(Integer levTypId) {
        if (levTypId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", levTypId);
    }

    @Override
    public int updateByPrimaryKeySelective(LevelType record) {
        if (record == null || record.getLevTypId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(LevelType record) {
        if (record == null || record.getLevTypId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
