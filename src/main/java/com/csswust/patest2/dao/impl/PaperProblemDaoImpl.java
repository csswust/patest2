package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.PaperProblemDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.result.SelectProblemNumRe;
import com.csswust.patest2.entity.PaperProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaperProblemDaoImpl extends CommonMapper<PaperProblem, BaseQuery> implements PaperProblemDao {
    private static Logger log = LoggerFactory.getLogger(PaperProblemDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.PaperProblemDao.";
    }

    @Override
    public void insertInit(PaperProblem record, Date date) {
        record.setPapProId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(PaperProblem record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public int deleteByExamId(Integer examId) {
        if (examId == null) {
            return 0;
        }
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("examId", examId);
            return getSqlSession().delete(getPackage() + "deleteByExamId", param);
        } catch (Exception e) {
            log.error("PaperProblemDaoImpl.deleteByExamId({}) error: {}", examId, e);
            return 0;
        }
    }

    @Override
    public List<SelectProblemNumRe> selectProblemNum(Integer examId) {
        if (examId == null) {
            return new ArrayList<>();
        }
        try {
            return getSqlSession().selectList(getPackage() + "selectProblemNum", examId);
        } catch (Exception e) {
            log.error("PaperProblemDaoImpl.selectProblemNum({}) error: {}", examId, e);
            return new ArrayList<>();
        }
    }
}
