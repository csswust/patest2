package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ExamParamDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ExamParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ExamParamDaoImpl extends CommonMapper<ExamParam, BaseQuery> implements ExamParamDao {
    private static Logger log = LoggerFactory.getLogger(ExamParamDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamParamDao.";
    }

    @Override
    public void insertInit(ExamParam record) {
        record.setExaParId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamParam record) {
        record.setModifyTime(new Date());
    }

    @Override
    public int getProblemNum(ExamParam record, BaseQuery query) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("record", record);
            param.put("query", query);
            if (query.getPage() != null && query.getRows() != null) {
                param.put("start", (query.getPage() - 1) * query.getRows());
                param.put("rows", query.getRows());
            }
            return getSqlSession().selectOne(getPackage() + "getProblemNum", param);
        } catch (Exception e) {
            log.error("ExamParamDaoImpl.getProblemNum({}) error: {}", record, e);
            return 0;
        }
    }
}
