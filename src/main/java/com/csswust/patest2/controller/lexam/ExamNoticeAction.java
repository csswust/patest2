package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ExamNoticeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamNotice;
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
@RequestMapping("/examNotice")
public class ExamNoticeAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ExamNoticeAction.class);

    @Autowired
    private ExamNoticeDao examNoticeDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectTempProblem(
            ExamNotice examNotice,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (examNotice == null) return null;
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        Integer total = examNoticeDao.selectByConditionGetCount(examNotice, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ExamNotice> examNoticeList = examNoticeDao.selectByCondition(examNotice, baseQuery);
        res.put("total", total);
        res.put("examNoticeList", examNoticeList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(ExamNotice examNotice) {
        Map<String, Object> res = new HashMap<>();
        int result = examNoticeDao.insertSelective(examNotice);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(ExamNotice examNotice) {
        Map<String, Object> res = new HashMap<>();
        int result = examNoticeDao.updateByPrimaryKeySelective(examNotice);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = examNoticeDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
