package com.csswust.patest2.test.dao;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
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

    @Test
    public void jisuanTestNum() {
        String path = "D:\\root\\installtion\\patest2\\judge\\testdata";
        for (int i = 0; i < 863; i++) {
            ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(i);
            if (problemInfo == null) continue;
            ProblemInfo record = new ProblemInfo();
            record.setProbId(problemInfo.getProbId());
            String proTestData = path + "/" + i;
            int count = 0, j = 0;
            long max = Config.getToLong(SiteKey.SELECT_PROBLEM_DATA_MAX, SiteKey.SELECT_PROBLEM_DATA_MAX_DE);
            while (true) {
                File inFile = new File(proTestData + "/" + j + ".in");
                File outFile = new File(proTestData + "/" + j + ".out");
                if (inFile.exists() && outFile.exists()) {
                    count++;
                    j++;
                    if (inFile.length() > max / 2) {
                        System.err.println(i);
                    }
                } else break;
            }
            if (count == 0) {
                continue;
            }
            int num = 100 / count, shengyu = 100 % count;
            String riod = "";
            for (j = 0; j < count; j++) {
                if (j != 0) riod += ",";
                if (j == count - 1) riod += (num + shengyu);
                else riod += num;
            }
            //System.out.println(count + " " + riod);
            /*record.setTestdataNum(count);
            record.setScoreRatio(riod);
            problemInfoDao.updateByPrimaryKeySelective(record);*/
        }
    }
}
