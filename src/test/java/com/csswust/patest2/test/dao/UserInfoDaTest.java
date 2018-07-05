package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import com.csswust.patest2.utils.CipherUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class UserInfoDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private ResultInfoDao resultInfoDao;

    @Autowired
    private ExamParamDao examParamDao;

    @Test
    public void test() {
        List<ResultInfo> infos = resultInfoDao.selectByCondition(new ResultInfo(),new BaseQuery());
        System.out.println(infos);
    }
    @Test
    public void test2() {
        ExamParam examParam = new ExamParam();
        examParam.setExamId(19);
        List<ExamParam> examParams = examParamDao.selectByCondition(examParam,new BaseQuery());
        System.out.println(examParams);
    }


    @Test
    public void insertSelective() {
        UserInfo userInfo = new UserInfo();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setCustom("userProfileIds", Arrays.asList(2, 6409));
        List<UserInfo> result = userInfoDao.selectByCondition(userInfo, baseQuery);
        System.out.println(JSON.toJSONString(result));
    }

    @Autowired
    private ExamProblemDao examProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;

    @Test
    public void update() {
        ExamProblem examProblem = new ExamProblem();
        examProblem.setExamId(23);
        List<ExamProblem> examProblemList = examProblemDao.selectByCondition(examProblem, new BaseQuery());
        for (int i = 0; i < examProblemList.size(); i++) {
            ExamProblem item = examProblemList.get(i);
            ProblemInfo problemInfo = new ProblemInfo();
            problemInfo.setProbId(item.getProblemId());
            problemInfo.setKnowId(33);
            problemInfoDao.updateByPrimaryKeySelective(problemInfo);
        }
    }
}
