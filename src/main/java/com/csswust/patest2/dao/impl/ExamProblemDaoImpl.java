package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ExamProblemDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ExamProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ExamProblemDaoImpl extends CommonMapper<ExamProblem, BaseQuery> implements ExamProblemDao {
    private static Logger log = LoggerFactory.getLogger(ExamProblemDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamProblemDao.";
    }

    @Override
    public void insertInit(ExamProblem record) {
        record.setExaProId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ExamProblem record) {
        record.setModifyTime(new Date());
    }

    @Override
    public List<ExamProblem> selectByProblem(ExamProblem record, BaseQuery query) {
        List<ExamProblem> list = new ArrayList<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("record", record);
            param.put("query", query);
            if (query != null && query.getPage() != null && query.getRows() != null) {
                param.put("start", (query.getPage() - 1) * query.getRows());
                param.put("rows", query.getRows());
            }
            return getSqlSession().selectList(getPackage() + "selectByProblem", param);
        } catch (Exception e) {
            log.error("ExamProblemDaoImpl.selectByProblem({}) error: {}", record, e);
            return list;
        }
    }
}
