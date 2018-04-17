package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpUserInfoDao;
import com.csswust.patest2.entity.EpUserInfo;
import com.csswust.patest2.utils.CipherUtil;
import org.apache.commons.lang3.StringUtils;
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
public class EpUserInfoAction extends BaseAction {
    @Autowired
    private EpUserInfoDao epUserInfoDao;

    @RequestMapping(value = "/epUserInfo/login", method = {RequestMethod.GET, RequestMethod.POST})
    public Object login(
            @RequestParam String username,
            @RequestParam String password) {
        APIResult apiResult = new APIResult();
        if (StringUtils.isBlank(username)) {
            apiResult.setStatusAndDesc(-1, "用户不能为空");
            return apiResult;
        }
        if (StringUtils.isBlank(password)) {
            apiResult.setStatusAndDesc(-2, "密码不能为空");
            return apiResult;
        }
        EpUserInfo epUserInfo = epUserInfoDao.selectByUsername(username);
        if (epUserInfo == null) {
            apiResult.setStatusAndDesc(-3, "用户不存在");
            return apiResult;
        }
        String newPass = CipherUtil.encode(password);
        if (!newPass.equals(epUserInfo.getPassword())) {
            apiResult.setStatusAndDesc(-4, "密码错误");
            return apiResult;
        }
        apiResult.setStatusAndDesc(1, "登录成功");
        apiResult.setDataKey("epUserId", epUserInfo.getUserId());
        apiResult.setDataKey("epUserName", epUserInfo.getUsername());
        saveSession(request, apiResult.getData());
        return apiResult;
    }

    @RequestMapping(value = "/epUserInfo/register", method = {RequestMethod.GET, RequestMethod.POST})
    public Object register(EpUserInfo epUserInfo) {
        APIResult apiResult = new APIResult();
        if (epUserInfo == null) {
            apiResult.setStatusAndDesc(-1, "epUserInfo不能为空");
            return apiResult;
        }
        EpUserInfo temp = epUserInfoDao.selectByUsername(epUserInfo.getUsername());
        if (temp != null) {
            apiResult.setStatusAndDesc(-2, "用户已存在");
            return apiResult;
        }
        temp = epUserInfoDao.selectByEmail(epUserInfo.getEmail());
        if (temp != null) {
            apiResult.setStatusAndDesc(-3, "邮箱已存在");
            return apiResult;
        }
        String newPass = CipherUtil.encode(epUserInfo.getPassword());
        epUserInfo.setPassword(newPass);
        int result = epUserInfoDao.insertSelective(epUserInfo);
        if (result != 1) {
            apiResult.setStatusAndDesc(-4, "注册失败");
            return apiResult;
        }
        apiResult.setStatus(1);
        return apiResult;
    }
}
