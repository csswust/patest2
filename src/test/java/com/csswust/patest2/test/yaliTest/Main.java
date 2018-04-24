package com.csswust.patest2.test.yaliTest;

import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 972536780 on 2018/4/2.
 */
public class Main extends JunitBaseServiceDaoTest {
    private static final String rootPath = "http://222.196.35.228:8081/patest";
    private static final Integer examId = 5;

    @Autowired
    private SubmitInfoDao submitInfoDao;

    private static ExecutorService judgeExecutor = Executors.newFixedThreadPool(419);

    @Test
    public void yalitest() throws Exception {
        List<People> peopleList = new ArrayList<>();
        Scanner in = new Scanner(new FileInputStream("E:\\javawork\\patest2\\src\\test\\java\\com\\csswust\\patest2\\test\\yaliTest\\user.txt"));
        while (in.hasNext()) {
            People people = new People();
            people.setUsername(in.next());
            people.setPassword(in.next());
            people.setRootPath(rootPath);
            people.setExamId(examId);
            people.setSubmitInfoDao(submitInfoDao);
            peopleList.add(people);
        }
        for (int i = 0; i < peopleList.size(); i++) {
            judgeExecutor.execute(peopleList.get(i));
        }
        Thread.sleep(1000000000);
    }
}
