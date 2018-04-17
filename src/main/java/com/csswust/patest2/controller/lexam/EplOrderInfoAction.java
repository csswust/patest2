package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.service.EpOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
public class EplOrderInfoAction extends BaseAction {
    @Autowired
    private EpOrderInfoDao epOrderInfoDao;
    @Autowired
    private EpOrderInfoService epOrderInfoService;

    @RequestMapping(value = "/epOrderInfo/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpOrderInfo epOrderInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return epOrderInfoService.selectByCondition(epOrderInfo, page, rows);
    }

    @RequestMapping(value = "/epOrderInfo/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertOne(EpOrderInfo epOrderInfo) {
        APIResult apiResult = new APIResult();
        int result = epOrderInfoDao.insertSelective(epOrderInfo);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/epOrderInfo/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(EpOrderInfo epOrderInfo) {
        APIResult apiResult = new APIResult();
        int result = epOrderInfoDao.updateByPrimaryKeySelective(epOrderInfo);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/epOrderInfo/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        int result = epOrderInfoDao.deleteByIds(ids);
        apiResult.setStatus(result);
        return apiResult;
    }
}
