package com.csswust.patest2.controller;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.SubmitResultDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.SubmitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/22.
 */
@RestController
@RequestMapping("/submitResult")
public class SubmitResultAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(SubmitInfoAction.class);

    @Autowired
    private SubmitResultDao submitResultDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            SubmitResult submitResult,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        Integer total = submitResultDao.selectByConditionGetCount(submitResult, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<SubmitResult> submitResultList = submitResultDao.selectByCondition(
                submitResult, baseQuery);
        res.put("total", total);
        res.put("submitResultList", submitResultList);
        return res;
    }
}
