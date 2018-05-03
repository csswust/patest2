package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.utils.CipherUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 972536780 on 2018/5/3.
 */
@RestController
@RequestMapping("/epUserInfo")
public class EplUserInfoAction extends BaseAction {
    @Autowired
    private EpUserInfoDao epUserInfoDao;

    @RequestMapping(value = "/selectByCondition.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpUserInfo epUserInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        APIResult apiResult = new APIResult();
        BaseQuery baseQuery = new BaseQuery();
        int total = epUserInfoDao.selectByConditionGetCount(epUserInfo, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<EpUserInfo> epUserInfoList = epUserInfoDao.selectByCondition(epUserInfo, baseQuery);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("epUserInfoList", epUserInfoList);
        return apiResult;
    }

    @RequestMapping(value = "/insertOne.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object insertOne(EpUserInfo epUserInfo) {
        APIResult apiResult = new APIResult();
        EpUserInfo condition = new EpUserInfo();
        condition.setEmail(epUserInfo.getEmail());
        int total = epUserInfoDao.selectByConditionGetCount(condition, new BaseQuery());
        if (total != 0) {
            apiResult.setStatusAndDesc(-1, "邮箱已被占用");
            return apiResult;
        }
        epUserInfo.setPassword(CipherUtil.encode(epUserInfo.getPassword()));
        int status = epUserInfoDao.insertSelective(epUserInfo);
        apiResult.setStatusAndDesc(status, status == 1 ? "添加成功" : "添加失败");
        return apiResult;
    }

    @RequestMapping(value = "/updateById.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object updateById(EpUserInfo epUserInfo) {
        APIResult apiResult = new APIResult();
        if (StringUtils.isNotBlank(epUserInfo.getPassword())) {
            epUserInfo.setPassword(CipherUtil.encode(epUserInfo.getPassword()));
        } else {
            epUserInfo.setPassword(null);// 防止无意更改
        }
        int status = epUserInfoDao.updateByPrimaryKeySelective(epUserInfo);
        apiResult.setStatusAndDesc(status, status == 1 ? "修改成功" : "修改失败");
        return apiResult;
    }

    @RequestMapping(value = "/deleteByIds.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        int result = epUserInfoDao.deleteByIds(ids);
        apiResult.setStatusAndDesc(result, result >= 1 ? "删除成功" : "删除失败");
        return apiResult;
    }

    @RequestMapping(value = "/active.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object active(@RequestParam Integer id) {
        APIResult apiResult = new APIResult();
        if (id == null) {
            apiResult.setStatusAndDesc(-1, "id不能为空");
            return apiResult;
        }
        EpUserInfo record = new EpUserInfo();
        record.setUserId(id);
        record.setIsActive(1);
        int result = epUserInfoDao.updateByPrimaryKeySelective(record);
        apiResult.setStatusAndDesc(result, result == 1 ? "激活成功" : "激活失败");
        return apiResult;
    }
}
