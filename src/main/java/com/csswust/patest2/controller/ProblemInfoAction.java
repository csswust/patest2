package com.csswust.patest2.controller;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.CourseInfoDao;
import com.csswust.patest2.dao.KnowledgeInfoDao;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.CourseInfo;
import com.csswust.patest2.entity.KnowledgeInfo;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.service.ProblemInfoService;
import com.csswust.patest2.service.result.SelectProblemDataRe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/problemInfo")
public class ProblemInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ProblemInfoAction.class);

    @Autowired
    private KnowledgeInfoDao knowledgeInfoDao;
    @Autowired
    private CourseInfoDao courseInfoDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private ProblemInfoService problemInfoService;

    @RequestMapping(value = "/uploadDataByFile", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadDataByFile(
            @RequestParam(required = true, value = "datafile") MultipartFile namefile,
            @RequestParam(required = true) Integer probId) {
        Map<String, Object> res = new HashMap<>();
        APIResult result = problemInfoService.insertProblemData(probId, namefile);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/uploadDataByForm", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadDataByForm(
            @RequestParam(required = true) String[] input,
            @RequestParam(required = true) String[] output,
            @RequestParam(required = true) Integer probId) {
        Map<String, Object> res = new HashMap<>();
        List<String> inputList = Arrays.asList(input);
        List<String> outputList = Arrays.asList(output);
        APIResult result = problemInfoService.insertProblemData(probId, inputList, outputList);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/selectProblemData", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectProblemData(
            @RequestParam(required = true) Integer probId) {
        Map<String, Object> res = new HashMap<>();
        SelectProblemDataRe re = problemInfoService.selectProblemData(probId);
        res.put("selectProblemDataRe", re);
        return res;
    }

    @RequestMapping(value = "/importProblmData", method = {RequestMethod.GET, RequestMethod.POST})
    public Object importProblmData(@RequestParam Integer probId) {
        if (probId == null) return null;
        return problemInfoService.importProblmData(probId);
    }

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ProblemInfo problemInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        BaseQuery baseQuery = new BaseQuery();
        if (StringUtils.isNotBlank(problemInfo.getTitle())) {
            baseQuery.setCustom("title", problemInfo.getTitle());
            problemInfo.setTitle(null);
        }
        Integer total = problemInfoDao.selectByConditionGetCount(problemInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ProblemInfo> problemInfoList = problemInfoDao.selectByCondition(problemInfo, baseQuery);
        List<KnowledgeInfo> knowledgeInfoList = selectRecordByIds(
                getFieldByList(problemInfoList, "knowId", ProblemInfo.class),
                "knowId", (BaseDao) knowledgeInfoDao, KnowledgeInfo.class);
        List<CourseInfo> courseInfoList = selectRecordByIds(
                getFieldByList(knowledgeInfoList, "courseId", KnowledgeInfo.class),
                "couId", (BaseDao) courseInfoDao, CourseInfo.class);
        res.put("total", total);
        res.put("data", problemInfoList);
        res.put("knowledge", knowledgeInfoList);
        res.put("course", courseInfoList);
        return res;
    }

    @RequestMapping(value = "/selectByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        List<ProblemInfo> problemInfoList = problemInfoDao.selectByIds(ids);
        res.put("problemInfoList", problemInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(ProblemInfo problemInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = problemInfoDao.insertSelective(problemInfo);
        res.put("status", result);
        res.put("proId", problemInfo.getProbId());
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(ProblemInfo problemInfo) {
        Map<String, Object> res = new HashMap<>();
        int result = problemInfoDao.updateByPrimaryKeySelective(problemInfo);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam(required = true) String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = problemInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
