package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpApplyInfoDao;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.entity.EpApplyInfo;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.service.EpApplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
public class EplApplyInfoAction extends BaseAction {
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

    @RequestMapping(value = "/epApplyInfo/accept", method = {RequestMethod.GET, RequestMethod.POST})
    public Object accept(
            @RequestParam Integer applyId,
            @RequestParam Integer status,
            @RequestParam(required = false) Double money,
            @RequestParam(required = false) String reason) {
        return epApplyInfoService.accept(applyId, status, money, reason);
    }

    @RequestMapping(value = "/epApplyInfo/deleteById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteById(@RequestParam Integer applyId) {
        APIResult apiResult = new APIResult();
        apiResult.setStatus(epApplyInfoDao.deleteByPrimaryKey(applyId));
        return apiResult;
    }

    @RequestMapping(value = "/epApplyInfo/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        apiResult.setStatus(epApplyInfoDao.deleteByIds(ids));
        return apiResult;
    }
}
