package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.ExamPaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ExamPaperDaoImpl extends CommonMapper<ExamPaper, BaseQuery> implements ExamPaperDao {
    private static Logger log = LoggerFactory.getLogger(ExamPaperDaoImpl.class);

    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ExamPaperDao.";
    }

    @Override
    public void insertInit(ExamPaper record, Date date) {
        record.setExaPapId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
    }

    @Override
    public void updatInit(ExamPaper record, Date date) {
        record.setModifyTime(date);
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
            log.error("ExamPaperDaoImpl.deleteByExamId({}) error: {}", examId, e);
            return 0;
        }
    }
}
