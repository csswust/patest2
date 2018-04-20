package com.csswust.patest2.test.yaliTest;

import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.PaperProblem;
import com.csswust.patest2.entity.SubmitInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 972536780 on 2018/4/2.
 */
public class People implements Runnable {
    private  String rootPath;
    private SubmitInfoDao submitInfoDao;
    private String username;
    private String password;
    private Integer examId;

    @Override
    public void run() {
        // 登录
        String JSESSIONID = login();
        System.out.println(JSESSIONID);
        if (JSESSIONID == null) return;
        // 查询题目
        String jsonResult = selectMyProblem(JSESSIONID);
        System.out.println(jsonResult);
        Gson gson = new Gson();
        MyProblem myProblem = gson.fromJson(jsonResult, new TypeToken<MyProblem>() {
        }.getType());
        if (myProblem == null) return;
        if (myProblem.getProblemInfoList() == null) return;
        if (myProblem.getPaperProblemList() == null) return;
        List<PaperProblem> paperProblemList = myProblem.getPaperProblemList();
        int proTotal = paperProblemList.size();
        Random random = new Random();
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setRows(10);
        SubmitInfo condition = new SubmitInfo();
        while (true) {
            // 休眠
            int sleep = random.nextInt(160);
            try {
                Thread.sleep(sleep * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int index = random.nextInt(proTotal);
            int paperProblemId = paperProblemList.get(index).getPapProId();
            int probId = paperProblemList.get(index).getProblemId();

            int page = random.nextInt(10) + 1;
            baseQuery.setPage(page);
            condition.setProblemId(probId);
            List<SubmitInfo> submitInfoList = submitInfoDao.selectByCondition(condition, baseQuery);
            if (submitInfoList == null || submitInfoList.size() == 0) continue;
            int subIndex = random.nextInt(submitInfoList.size());
            SubmitInfo submitInfo = submitInfoList.get(subIndex);
            String submRsult = insertSubmitInfo(JSESSIONID, submitInfo.getSource(), paperProblemId, submitInfo.getJudgerId());
            System.out.println(submRsult);
        }
    }

    public String insertSubmitInfo(String JSESSIONID, String source, Integer paperProblemId, Integer judgerId) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("examId", examId);
            param.put("source", source);
            param.put("paperProblemId", paperProblemId);
            param.put("judgerId", judgerId);
            return HttpRequest.sendPost(rootPath + "/student/insertSubmitInfo", param, JSESSIONID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String selectMyProblem(String JSESSIONID) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("examId", examId);
            return HttpRequest.sendPost(rootPath + "/student/selectMyProblem", param, JSESSIONID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String login() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("username", username);
            param.put("password", password);
            Map<String, Object> setCookies = new HashMap<>();
            String result = HttpRequest.sendPost(rootPath + "/userInfo/login", param, setCookies);
            return (String) setCookies.get("JSESSIONID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SubmitInfoDao getSubmitInfoDao() {
        return submitInfoDao;
    }

    public void setSubmitInfoDao(SubmitInfoDao submitInfoDao) {
        this.submitInfoDao = submitInfoDao;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
