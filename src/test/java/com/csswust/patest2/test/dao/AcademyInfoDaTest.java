package com.csswust.patest2.test.dao;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AcademyInfoDaTest extends JunitBaseServiceDaoTest {
    @Autowired
    private AcademyInfoDao academyInfoDao;

    @Test
    public void insertSelective() {
        BaseDao baseDao = (BaseDao) academyInfoDao;
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(2);
        List<AcademyInfo> result = baseDao.selectByIdsList(list);
        System.out.println(JSON.toJSONString(result));
    }
}
