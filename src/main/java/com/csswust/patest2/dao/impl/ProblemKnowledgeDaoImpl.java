package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.ProblemKnowledgeDao;
import com.csswust.patest2.entity.ProblemKnowledge;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ProblemKnowledgeDaoImpl extends CommonMapper<ProblemKnowledge, BaseQuery> implements ProblemKnowledgeDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.ProblemKnowledgeDao.";
    }

    @Override
    public void insertInit(ProblemKnowledge record) {
        record.setProKnoId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(ProblemKnowledge record) {
        record.setModifyTime(new Date());
    }
}
