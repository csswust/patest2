package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.MajorInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.entity.MajorInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.UserProfileService;
import com.csswust.patest2.service.result.UserProfileLoadRe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/12.
 */
@RestController
@RequestMapping("/userProfile")
public class UserProfileAction extends BaseAction {
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private MajorInfoDao majorInfoDao;
    @Autowired
    private AcademyInfoDao academyInfoDao;
    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            UserProfile userProfile,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<UserProfile> userProfileList = userProfileDao.selectByCondition(userProfile,
                new BaseQuery(page, rows));
        Integer total = userProfileDao.selectByConditionGetCount(userProfile, new BaseQuery());
        List<MajorInfo> majorInfoList = selectRecordByIds(
                getFieldByList(userProfileList, "majorId", UserProfile.class),
                "majId", (BaseDao) majorInfoDao, MajorInfo.class);
        List<AcademyInfo> academyInfoList = selectRecordByIds(
                getFieldByList(majorInfoList, "academyId", MajorInfo.class),
                "acaId", (BaseDao) academyInfoDao, AcademyInfo.class);
        res.put("total", total);
        res.put("list", userProfileList);
        res.put("majorInfoList", majorInfoList);
        res.put("academyInfoList", academyInfoList);
        return res;
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(UserProfile userProfile) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.insertSelective(userProfile);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(UserProfile userProfile) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.updateByPrimaryKeySelective(userProfile);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        Map<String, Object> res = new HashMap<String, Object>();
        int result = userProfileDao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/insertByExcel", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertByExcel(
            @RequestParam MultipartFile namefile,
            @RequestParam(required = false, defaultValue = "false") Boolean isIgnoreError) {
        Map<String, Object> res = new HashMap<String, Object>();
        UserProfileLoadRe result = userProfileService.insertByExcel(namefile, isIgnoreError);
        res.put("userProfileLoadRe", result);
        return res;
    }
}
