package com.csswust.patest2.controller;

import com.csswust.patest2.common.paramJudge.StringCallBack;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ResultInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/resultInfo")
public class ResultInfoAction extends BaseAction {
    @Autowired
    private ResultInfoDao resultInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ResultInfo resultInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        resultInfo = paramVerificate(resultInfo, new StringCallBack());
        Map<String, Object> res = new HashMap<>();
        List<ResultInfo> resultInfoList = resultInfoDao.selectByCondition(resultInfo,
                new BaseQuery(page, rows));
        Integer total = resultInfoDao.selectByConditionGetCount(resultInfo, new BaseQuery());
        res.put("total", total);
        res.put("data", resultInfoList);
        return res;
    }
}
