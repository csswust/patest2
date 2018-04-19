package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpOrderInfoDao;
import com.csswust.patest2.entity.EpOrderInfo;
import com.csswust.patest2.service.EpOrderInfoService;
import com.csswust.patest2.service.common.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/ep")
public class EpOrderInfoAction extends BaseAction {
    @Autowired
    private EpOrderInfoService epOrderInfoService;
    @Autowired
    private EpOrderInfoDao epOrderInfoDao;
    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/epOrderInfo/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpOrderInfo epOrderInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (epOrderInfo == null) return new APIResult(-501, "epOrderInfo不能为空");
        epOrderInfo.setEpUserId(getEpUserId());
        return epOrderInfoService.selectByCondition(epOrderInfo, page, rows);
    }

    @RequestMapping(value = "/epOrderInfo/payment", method = {RequestMethod.GET, RequestMethod.POST})
    public Object payment(@RequestParam Integer orderId) {
        EpOrderInfo epOrderInfo = epOrderInfoDao.selectByPrimaryKey(orderId);
        if (epOrderInfo == null) {
            return new APIResult(-501, "orderId无效");
        } else {
            if (getEpUserId().intValue() != epOrderInfo.getEpUserId().intValue()) {
                return new APIResult(-501, "权限不足");
            }
        }
        return epOrderInfoService.payment(orderId);
    }
}
