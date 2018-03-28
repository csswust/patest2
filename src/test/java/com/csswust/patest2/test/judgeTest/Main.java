package com.csswust.patest2.test.judgeTest;

import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.dao.ProblemSubmitDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.ProblemSubmit;
import com.csswust.patest2.entity.SubmitResult;
import com.csswust.patest2.test.JunitBaseServiceDaoTest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

class Result {
    private Integer submId;
    private Integer status;

    public Integer getSubmId() {
        return submId;
    }

    public void setSubmId(Integer submId) {
        this.submId = submId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

public class Main extends JunitBaseServiceDaoTest {
    @Autowired
    private ProblemSubmitDao problemSubmitDao;

    public static void main(String[] args) {

    }

    @Test
    public void test() throws InterruptedException {
        int total = problemSubmitDao.selectByConditionGetCount(new ProblemSubmit(), new BaseQuery());
        int len = total % 10 != 0 ? total / 10 + 1 : total / 10;
        ProblemSubmit condition = new ProblemSubmit();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setRows(10);
        List<ProblemSubmit> problemSubmitList;
        String cookies = "JSESSIONID=4BD5F23257E333C3D4F45A4233149F35";
        String url = "http://39.108.123.89:8080/patest2/submitInfo/testData";
        Random random = new Random();
        Map<String, Object> params = new HashMap<>();
        for (int i = 2272; i < len; i++) {
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
                Gson gson = new Gson();
                Result model = gson.fromJson(result, new TypeToken<Result>() {
                }.getType());
                if (model.getSubmId() != null) {
                    ProblemSubmit record = new ProblemSubmit();
                    record.setId(item.getId());
                    record.setNewSubmId(model.getSubmId());
                    problemSubmitDao.updateByPrimaryKeySelective(record);
                }
                //int sleep = random.nextInt(5);
                Thread.sleep(300);
            }
        }
    }
}
