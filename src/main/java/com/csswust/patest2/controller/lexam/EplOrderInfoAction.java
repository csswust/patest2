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
@RequestMapping("/epOrderInfo")
public class EplOrderInfoAction extends BaseAction {
    @Autowired
    private EpOrderInfoDao epOrderInfoDao;
    @Autowired
    private EpOrderInfoService epOrderInfoService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpOrderInfo epOrderInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return epOrderInfoService.selectByCondition(epOrderInfo, page, rows);
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertOne(EpOrderInfo epOrderInfo) {
        APIResult apiResult = new APIResult();
        int result = epOrderInfoDao.insertSelective(epOrderInfo);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(EpOrderInfo epOrderInfo) {
        APIResult apiResult = new APIResult();
        // 只能修改金额
        EpOrderInfo record = new EpOrderInfo();
        record.setMoney(epOrderInfo.getMoney());
        record.setOrderId(epOrderInfo.getOrderId());
        int result = epOrderInfoDao.updateByPrimaryKeySelective(record);
        apiResult.setStatusAndDesc(result, result == 1 ? "成功" : "失败");
        return apiResult;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        int result = epOrderInfoDao.deleteByIds(ids);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/payment", method = {RequestMethod.GET, RequestMethod.POST})
    public Object payment(@RequestParam Integer orderId) {
        EpOrderInfo epOrderInfo = epOrderInfoDao.selectByPrimaryKey(orderId);
        if (epOrderInfo == null) {
            return new APIResult(-501, "orderId无效");
        }
        return epOrderInfoService.payment(orderId);
    }
}
