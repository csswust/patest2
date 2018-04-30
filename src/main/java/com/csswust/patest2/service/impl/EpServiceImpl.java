package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.EpService;
import com.csswust.patest2.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/4/28.
 */
@Service
public class EpServiceImpl extends BaseService implements EpService {
    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private UserProfileDao userProfileDao;

    @Override
    public APIResult selectUserInfoList(List<UserInfo> userInfoList) {
        APIResult apiResult = new APIResult();
        if (userInfoList == null || userInfoList.size() == 0) {
            apiResult.setStatusAndDesc(-1, "数据为空");
            return apiResult;
        }
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<List<ExamPaper>> examPaperLists = new ArrayList<>();
        List<List<ExamInfo>> ExamInfoLists = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < userInfoList.size(); i++) {
            if (userInfoList.get(i) == null || userInfoList.get(i).getUserId() == null) {
                examPaperLists.add(new ArrayList<>());
                continue;
            }
            ExamPaper examPaper = new ExamPaper();
            examPaper.setUserId(userInfoList.get(i).getUserId());
            List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, new BaseQuery());
            count += examPaperList.size();
            List<ExamInfo> examInfoList = selectRecordByIds(
                    getFieldByList(examPaperList, "examId", ExamPaper.class),
                    "examId", (BaseDao) examInfoDao, ExamInfo.class);
            examPaperLists.add(examPaperList);
            ExamInfoLists.add(examInfoList);
        }
        if (count == 0) {
            apiResult.setStatusAndDesc(-1, "无认证记录");
        } else {
            apiResult.setStatusAndDesc(count, "成功");
            apiResult.setDataKey("userInfoList", userInfoList);
            apiResult.setDataKey("userProfileList", userProfileList);
            apiResult.setDataKey("examPaperLists", examPaperLists);
            apiResult.setDataKey("ExamInfoLists", ExamInfoLists);
        }
        return apiResult;
    }
}
