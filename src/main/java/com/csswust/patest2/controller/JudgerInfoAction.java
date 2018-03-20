package com.csswust.patest2.controller;

import com.csswust.patest2.common.paramJudge.StringCallBack;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.JudgerInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.JudgerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/19.
 */
@RestController
@RequestMapping("/judgerInfo")
public class JudgerInfoAction extends BaseAction {
    @Autowired
    private JudgerInfoDao judgerInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            JudgerInfo judgerInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        judgerInfo = paramVerificate(judgerInfo, new StringCallBack());
        Map<String, Object> res = new HashMap<>();
        List<JudgerInfo> resultInfoList = judgerInfoDao.selectByCondition(judgerInfo,
                new BaseQuery(page, rows));
        Integer total = judgerInfoDao.selectByConditionGetCount(judgerInfo, new BaseQuery());
        res.put("total", total);
        res.put("data", resultInfoList);
        return res;
    }
}
