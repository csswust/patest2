package com.csswust.patest2.controller;

import com.csswust.patest2.common.paramJudge.StringCallBack;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.entity.ProblemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/knowledgeInfo")
public class KnowledgeInfoAction extends BaseAction {
    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            KnowledgeInfo knowledgeInfo,
            @RequestParam(required = false, defaultValue = "true") Boolean containSum,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        knowledgeInfo = paramVerificate(knowledgeInfo, new StringCallBack());
        Map<String, Object> res = new HashMap<>();
        Integer total = knowledgeInfoDao.selectByConditionGetCount(knowledgeInfo,
                new BaseQuery(1, 1));
        List<KnowledgeInfo> knowledgeInfoList = knowledgeInfoDao.selectByCondition(
                knowledgeInfo, new BaseQuery(page, rows));
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        //查询对应知识点包含的题目数量
        if (containSum) {
            List<Integer> sumList = new ArrayList<>();
            for (int i = 0; i < knowledgeInfoList.size(); i++) {
                Integer knowId = knowledgeInfoList.get(i).getKnowId();
                ProblemInfo problemInfo = new ProblemInfo();
                problemInfo.setKnowId(knowId);
                Integer sum = problemInfoDao.selectByConditionGetCount(problemInfo, new BaseQuery());
                sumList.add(sum);
            }
            res.put("sumList", sumList);
        }
        res.put("total", total);
        res.put("knowledgeInfoList", knowledgeInfoList);
        res.put("courseInfoList", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(KnowledgeInfo knowledgeInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = knowledgeInfoDao.insertSelective(knowledgeInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(KnowledgeInfo knowledgeInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = knowledgeInfoDao.updateByPrimaryKeySelective(knowledgeInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = knowledgeInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
