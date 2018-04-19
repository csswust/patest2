package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpNoticeDao;
import com.csswust.patest2.entity.EpNotice;
import com.csswust.patest2.service.EpNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/epNotice")
public class EplNoticeAction extends BaseAction {
    @Autowired
    private EpNoticeDao epNoticeDao;
    @Autowired
    private EpNoticeService epNoticeService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpNotice epNotice,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return epNoticeService.selectByCondition(epNotice, page, rows);
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertOne(EpNotice epNotice) {
        APIResult apiResult = new APIResult();
        int result = epNoticeDao.insertSelective(epNotice);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(EpNotice epNotice) {
        APIResult apiResult = new APIResult();
        int result = epNoticeDao.updateByPrimaryKeySelective(epNotice);
        apiResult.setStatus(result);
        return apiResult;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        int result = epNoticeDao.deleteByIds(ids);
        apiResult.setStatus(result);
        return apiResult;
    }
}
