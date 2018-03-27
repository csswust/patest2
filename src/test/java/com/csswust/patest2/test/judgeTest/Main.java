package com.csswust.patest2.test.judgeTest;

import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.ProblemSubmitDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.ProblemSubmit;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 972536780 on 2018/3/27.
 */
public class Main extends JunitBaseServiceDaoTest {
    @Autowired
    private ProblemSubmitDao problemSubmitDao;

    @Test
    public void test() {
        int total = problemSubmitDao.selectByConditionGetCount(new ProblemSubmit(), new BaseQuery());
        int len = total % 10 != 0 ? total / 10 + 1 : total / 10;
        ProblemSubmit condition = new ProblemSubmit();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setRows(10);
        List<ProblemSubmit> problemSubmitList;
        String cookies = "JSESSIONID=6A13CDDD956D2A158972C6029B8C9B1C";
        String url = "http://39.108.123.89:8080/patest2/submitInfo/testData";
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < 1; i++) {
            baseQuery.setPage(i + 1);
            problemSubmitList = problemSubmitDao.selectByCondition(condition, baseQuery);
            for (ProblemSubmit item : problemSubmitList) {
                String source = item.getSource();
                Integer judgerId = item.getJudgerId();
                Integer problemId = item.getProblemId();
                if (StringUtils.isBlank(source) || judgerId == null || problemId == null) continue;
                params.put("source", source);
                params.put("judgerId", judgerId);
                params.put("problemId", problemId);
                String result = HttpRequest.sendPost(url, params, cookies);
                System.out.println(result);
            }
        }
    }
}
