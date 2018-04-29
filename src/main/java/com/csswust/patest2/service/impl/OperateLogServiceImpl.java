package com.csswust.patest2.service.impl;

import com.csswust.patest2.dao.OperateLogDao;
import com.csswust.patest2.entity.OperateLog;
import com.csswust.patest2.service.OperateLogService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.input.OperateLogInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 972536780 on 2018/4/28.
 */
@Service
public class OperateLogServiceImpl extends BaseService implements OperateLogService {
    private static Logger log = LoggerFactory.getLogger(OperateLogServiceImpl.class);

    @Autowired
    private OperateLogDao operateLogDao;

    @Override
    public int insertOne(OperateLogInsert insert) {
        OperateLog operateLog = new OperateLog();
        operateLog.setUserId(insert.getUserId());
        operateLog.setIp(insert.getIp());
        operateLog.setApiUrl(insert.getApiUrl());
        operateLog.setApiDesc(insert.getApiDesc());
        operateLog.setApiArgc(getJson(insert.getArgc()));
        operateLog.setExamId(insert.getExamId());
        operateLog.setProblemId(insert.getProblemId());
        return operateLogDao.insertSelective(operateLog);
    }
}
