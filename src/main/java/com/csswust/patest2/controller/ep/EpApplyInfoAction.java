package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.service.EpApplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/ep")
public class EpApplyInfoAction extends BaseAction {
    @Autowired
    private EpApplyInfoDao epApplyInfoDao;

    @Autowired
    private EpApplyInfoService epApplyInfoService;

    @RequestMapping(value = "/epApplyInfo/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpApplyInfo epApplyInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (epApplyInfo == null) return null;
        epApplyInfo.setEpUserId(getEpUserId());
        return epApplyInfoService.selectByCondition(epApplyInfo, page, rows);
    }

    @RequestMapping(value = "/epApplyInfo/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insert(
            @RequestParam String examName,
            @RequestParam Integer peopleNumber,
            @RequestParam Integer isProv,
            @RequestParam Date startTime,
            @RequestParam Date endTime) {
        APIResult apiResult = new APIResult();
        EpApplyInfo epApplyInfo = new EpApplyInfo();
        epApplyInfo.setExamName(examName);
        epApplyInfo.setPeopleNumber(peopleNumber);
        epApplyInfo.setIsProblem(isProv);
        epApplyInfo.setStartTime(startTime);
        epApplyInfo.setEndTime(endTime);
        epApplyInfo.setEpUserId(getEpUserId());
        epApplyInfo.setStatus(0);
        int result = epApplyInfoDao.insertSelective(epApplyInfo);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/epApplyInfo/deleteById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteById(@RequestParam Integer applyId) {
        APIResult apiResult = new APIResult();
        apiResult.setStatus(epApplyInfoDao.deleteByPrimaryKey(applyId));
        return apiResult;
    }
}
