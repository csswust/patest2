package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.PaperProblemDao;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaperProblemDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private PaperProblemDao paperProblemDao;

    @Test
    public void selectProblemNum() {
        System.out.println(JSON.toJSONString(paperProblemDao.selectProblemNum(1)));
    }
}
