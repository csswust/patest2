package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.SubmitResultDao;
import com.csswust.patest2.entity.SubmitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SubmitResultDaoImpl extends CommonMapper<SubmitResult, BaseQuery> implements SubmitResultDao {
    private static Logger log = LoggerFactory.getLogger(SubmitResultDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SubmitResultDao.";
    }

    @Override
    public void insertInit(SubmitResult record) {
        record.setSubResId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(SubmitResult record) {
        record.setModifyTime(new Date());
    }

    @Override
    public int deleteBySubmId(Integer submId) {
        if (submId == null) {
            return 0;
        }
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("submId", submId);
            return getSqlSession().delete(getPackage() + "deleteBySubmId", param);
        } catch (Exception e) {
            log.error("SubmitResultDaoImpl.deleteBySubmId({}) error: {}", submId, e);
            return 0;
        }
    }
}
