package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.EpNoticeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpNotice;
import com.csswust.patest2.service.EpNoticeService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/17.
 */
@Service
public class EpNoticeServiceImpl extends BaseService implements EpNoticeService {
    @Autowired
    private EpNoticeDao epNoticeDao;

    @Override
    public APIResult selectByCondition(EpNotice epNotice, Integer page, Integer rows) {
        if (epNotice == null) return null;
        APIResult apiResult = new APIResult();
        List<EpNotice> epNoticeList = epNoticeDao.selectByCondition(epNotice,
                new BaseQuery(page, rows));
        Integer total = epNoticeDao.selectByConditionGetCount(epNotice, new BaseQuery());
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("list", epNoticeList);
        apiResult.setStatus(1);
        return apiResult;
    }
}
