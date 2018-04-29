package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserLoginLogDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserLoginLog;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.common.ConditionBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/28.
 */
@RestController
@RequestMapping("/userLoginLog")
public class UserLoginLogAction extends BaseAction {
    @Autowired
    private ConditionBuild conditionBuild;
    @Autowired
    private UserLoginLogDao userLoginLogDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            UserLoginLog userLoginLog,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date startTime,
            @RequestParam(required = false) Date endTime,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        APIResult apiResult = new APIResult();
        BaseQuery baseQuery = new BaseQuery();
        if (startTime != null) baseQuery.setCustom("startTime", startTime);
        if (endTime != null) baseQuery.setCustom("endTime", endTime);
        conditionBuild.buildUserLoginLog(baseQuery, userLoginLog, username, studentNumber);
        Integer total = userLoginLogDao.selectByConditionGetCount(userLoginLog, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<UserLoginLog> userLoginLogList = userLoginLogDao.selectByCondition(userLoginLog, baseQuery);
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(userLoginLogList, "userId", UserLoginLog.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("userLoginLogList", userLoginLogList);
        apiResult.setDataKey("userInfoList", userInfoList);
        apiResult.setDataKey("userProfileList", userProfileList);
        return apiResult;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteByIds(@RequestParam String ids) {
        APIResult apiResult = new APIResult();
        int result = userLoginLogDao.deleteByIds(ids);
        apiResult.setStatus(result);
        return apiResult;
    }
}
