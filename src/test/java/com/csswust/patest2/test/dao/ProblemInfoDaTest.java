package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProblemInfoDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private ProblemInfoDao problemInfoDao;

    @Test
    public void insertSelective() {
        Random random = new Random();
        int count = 0;
        for (int i = 1; i < 800; i++) {
            ProblemInfo problemInfo = new ProblemInfo();
            problemInfo.setProbId(i);
            problemInfo.setKnowId(random.nextInt(30) + 1);
            count += problemInfoDao.updateByPrimaryKeySelective(problemInfo);
        }
        System.out.println(count);
    }
}
