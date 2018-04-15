package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.PaperProblemDao;
import com.csswust.patest2.entity.PaperProblem;
import com.csswust.patest2.service.JudgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/23.
 */
@RestController
@RequestMapping("/paperProblem")
public class PaperProblemAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(PaperProblemAction.class);

    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private JudgeService judgeService;

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(PaperProblem paperProblem) {
        Map<String, Object> res = new HashMap<>();
        int result = paperProblemDao.insertSelective(paperProblem);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(PaperProblem paperProblem) {
        Map<String, Object> res = new HashMap<>();
        // 注意,更新后要更新对应的paper
        int result = paperProblemDao.updateByPrimaryKeySelective(paperProblem);
        judgeService.refreshPaperById(paperProblem.getPapProId());
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = paperProblemDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
