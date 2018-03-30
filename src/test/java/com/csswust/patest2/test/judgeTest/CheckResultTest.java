package com.csswust.patest2.test.judgeTest;

import com.csswust.patest2.dao.ProblemSubmitDao;
import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.entity.ProblemSubmit;
import com.csswust.patest2.entity.SubmitInfo;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by 972536780 on 2018/3/30.
 */
public class CheckResultTest extends JunitBaseServiceDaoTest {
    @Autowired
    private ProblemSubmitDao problemSubmitDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;

    @Test
    public void test() throws InterruptedException, IOException {
        int count = 0;
        for (int i = 0; i < 158365; i++) {
            ProblemSubmit problemSubmit = problemSubmitDao.selectByPrimaryKey(i);
            if (problemSubmit == null) continue;
            if (problemSubmit.getStatus() == null) continue;
            if (problemSubmit.getNewSubmId() == null) continue;
            Integer newSubmId = problemSubmit.getNewSubmId();
            SubmitInfo submitInfo = submitInfoDao.selectByPrimaryKey(newSubmId);
            if (submitInfo == null) return;
            if (submitInfo.getStatus() == null) continue;
            int oldStatus = problemSubmit.getStatus();
            int newStatus = submitInfo.getStatus();
            update(i, newStatus);
            if (oldStatus + 1 != newStatus) {
                System.out.printf("%d %d %d %d %d %d\n", i, oldStatus + 1, newSubmId,
                        newStatus, problemSubmit.getUserId(), problemSubmit.getProblemId());
                count++;
                // if (count >= 10) break;
            }
        }
    }

    public void update(Integer submId, int newStatus) {
        ProblemSubmit record = new ProblemSubmit();
        record.setId(submId);
        record.setNewStatus(newStatus);
        problemSubmitDao.updateByPrimaryKeySelective(record);
    }
}
