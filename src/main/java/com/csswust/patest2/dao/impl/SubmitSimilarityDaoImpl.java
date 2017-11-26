package com.csswust.patest2.dao.impl;

import com.csswust.patest2.entity.SubmitSimilarity;
import com.csswust.patest2.dao.CommonMapper;
import com.csswust.patest2.dao.SubmitSimilarityDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SubmitSimilarityDaoImpl extends CommonMapper implements SubmitSimilarityDao {
    private final static String PACKAGE = "com.csswust.patest2.dao.SubmitSimilarityDao.";

    @Override
    public int deleteByPrimaryKey(Integer subSimId) {
        if (subSimId == null) {
            return 0;
        }
        return getSqlSession().delete(PACKAGE + "deleteByPrimaryKey", subSimId);
    }

    @Override
    public int insert(SubmitSimilarity record) {
        if (record == null) {
            return 0;
        }
        record.setSubSimId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insert", record);
    }

    @Override
    public int insertSelective(SubmitSimilarity record) {
        if (record == null) {
            return 0;
        }
        record.setSubSimId(null);
        record.setCreateTime(new Date());
        return getSqlSession().insert(PACKAGE + "insertSelective", record);
    }

    @Override
    public SubmitSimilarity selectByPrimaryKey(Integer subSimId) {
        if (subSimId == null) {
            return null;
        }
        return getSqlSession().selectOne(PACKAGE + "selectByPrimaryKey", subSimId);
    }

    @Override
    public int updateByPrimaryKeySelective(SubmitSimilarity record) {
        if (record == null || record.getSubSimId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(SubmitSimilarity record) {
        if (record == null || record.getSubSimId() == null) {
            return 0;
        }
        record.setModifyTime(new Date());
        return getSqlSession().update(PACKAGE + "updateByPrimaryKey", record);
    }
}
