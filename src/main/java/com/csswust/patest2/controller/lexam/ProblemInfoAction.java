package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.service.ProblemInfoService;
import com.csswust.patest2.service.result.SelectProblemDataRe;
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

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/problemInfo")
public class ProblemInfoAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(ProblemInfoAction.class);

    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private ProblemInfoService problemInfoService;

    @RequestMapping(value = "/uploadDataByFile", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadDataByFile(
            @RequestParam(value = "datafile") MultipartFile namefile,
            @RequestParam Integer probId) {
        if (probId == null) return null;
        Map<String, Object> res = new HashMap<>();
        APIResult result = problemInfoService.insertProblemData(probId, namefile);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/uploadDataByForm", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> uploadDataByForm(
            @RequestParam String[] input,
            @RequestParam String[] output,
            @RequestParam Integer probId) {
        Map<String, Object> res = new HashMap<>();
        for (int i = 0; i < output.length; i++) {
          if (output[i] != null) {
            output[i] = output[i].substring(0, output[i].length() - 1);
          }
        }
        List<String> inputList = Arrays.asList(input);
        List<String> outputList = Arrays.asList(output);
        APIResult result = problemInfoService.insertProblemData(probId, inputList, outputList);
        res.put("APIResult", result);
        return res;
    }

    @RequestMapping(value = "/selectProblemData", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectProblemData(@RequestParam Integer probId) {
        if (probId == null) return null;
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
    public Object selectByCondition(
            ProblemInfo problemInfo,
            @RequestParam(required = false, defaultValue = "false") Boolean containUModify,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return problemInfoService.selectByCondition(problemInfo, containUModify,
                true, page, rows);
    }

    @RequestMapping(value = "/selectByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        List<ProblemInfo> problemInfoList = problemInfoDao.selectByIds(ids);
        res.put("problemInfoList", problemInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertOne(ProblemInfo problemInfo) {
        APIResult apiResult = new APIResult();
        if (judgeTitle(null, problemInfo.getTitle())) {
            apiResult.setStatusAndDesc(-1, "标题不能重复");
            return apiResult;
        }
        int result = problemInfoDao.insertSelective(problemInfo);
        apiResult.setStatus(result);
        apiResult.setDataKey("proId", problemInfo.getProbId());
        return apiResult;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(ProblemInfo problemInfo) {
        APIResult apiResult = new APIResult();
        if (problemInfo == null) {
            apiResult.setStatusAndDesc(-1, "problemInfo不能为空");
            return apiResult;
        }
        if (problemInfo.getProbId() == null) {
            apiResult.setStatusAndDesc(-1, "probId不能为空");
            return apiResult;
        }
        if (judgeTitle(problemInfo.getProbId(), problemInfo.getTitle())) {
            apiResult.setStatusAndDesc(-1, "标题不能重复");
            return apiResult;
        }
        int result = problemInfoDao.updateByPrimaryKeySelective(problemInfo);
        apiResult.setStatus(result);
        return apiResult;
    }

    private boolean judgeTitle(Integer probId, String title) {
        ProblemInfo problemInfo = new ProblemInfo();
        problemInfo.setTitle(title);
        List<ProblemInfo> problemInfoList = problemInfoDao.selectByCondition(
                problemInfo, new BaseQuery(1, 2));
        // 未查到
        if (problemInfoList == null || problemInfoList.size() == 0) return false;
        // 插入时查到
        if (probId == null) return true;
        else if (problemInfoList.size() > 1) return true;
        else {
            // 更新时查到
            Integer currId = problemInfoList.get(0).getProbId();
            return currId != null && probId.intValue() != currId.intValue();
        }
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<>();
        int result = problemInfoDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }
}
