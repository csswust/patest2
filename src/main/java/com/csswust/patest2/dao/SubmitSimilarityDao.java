package com.csswust.patest2.dao;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SubmitSimilarity;
import com.csswust.patest2.vo.SimExportVo;

import java.util.List;

public interface SubmitSimilarityDao {
    int deleteByPrimaryKey(Integer subSimId);

    int insert(SubmitSimilarity record);

    int insertSelective(SubmitSimilarity record);

    SubmitSimilarity selectByPrimaryKey(Integer subSimId);

    int updateByPrimaryKeySelective(SubmitSimilarity record);

    int updateByPrimaryKey(SubmitSimilarity record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<SubmitSimilarity> selectByCondition(SubmitSimilarity record, BaseQuery query);

    int selectByConditionGetCount(SubmitSimilarity record, BaseQuery query);

    int insertBatch(List<SubmitSimilarity> recordList);

    List<SimExportVo> querySimByExamId(Double sim, Integer exam_id);
}