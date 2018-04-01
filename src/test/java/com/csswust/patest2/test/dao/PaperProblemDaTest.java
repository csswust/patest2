package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.PaperProblemDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import com.csswust.patest2.utils.CipherUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class PaperProblemDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private PaperProblemDao paperProblemDao;

    @Test
    public void selectProblemNum() {
        System.out.println(JSON.toJSONString(paperProblemDao.selectProblemNum(1)));
    }
}
