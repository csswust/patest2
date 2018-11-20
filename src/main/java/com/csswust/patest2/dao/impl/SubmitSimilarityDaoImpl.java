package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.SubmitSimilarityDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.SubmitSimilarity;
import com.csswust.patest2.vo.SimExportVo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubmitSimilarityDaoImpl extends CommonMapper<SubmitSimilarity, BaseQuery> implements SubmitSimilarityDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SubmitSimilarityDao.";
    }

    @Override
    public void insertInit(SubmitSimilarity record, Date date) {
        record.setSubSimId(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

    @Override
    public void updateInit(SubmitSimilarity record, Date date) {
        record.setModifyTime(date);
        record.setModifyUserId(getUserId());
    }

  @Override
  public List<SimExportVo> querySimByExamId(Double sim, Integer exam_id) {
    Map<String, Object> param = new HashMap<>();
    param.put("sim",sim);
    param.put("exam_id",exam_id);
    return getSqlSession().selectList(getPackage() + "querySimByExamId",param);
  }
}
