package com.csswust.patest2.test.dao;

import com.csswust.patest2.dao.SubmitInfoDao;
import com.csswust.patest2.entity.SubmitInfo;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SubmitInfoDaoTest extends JunitBaseServiceDaoTest {

  @Autowired
  private SubmitInfoDao dao;

  @Test
  public void test() throws IOException, InterruptedException {
    SubmitInfo record = new SubmitInfo();
    record.setExamId(38);
    record.setStatus(8);
    List<SubmitInfo> infos = dao.selectByCondition(record, null);
    System.out.println(infos.size());
    for (SubmitInfo info : infos) {
      URL url = new URL("http://222.196.35.228:8081/patest/submitInfo/rejudgeBySubmId?submId=" + info.getSubmId());
      URLConnection conn = url.openConnection();
      conn.setRequestProperty("Cookie", "JSESSIONID=300F8AC23DE10F5EEB6F0EB4699B6A47");
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
      String lines;
      while ((lines = reader.readLine()) != null) {
        if (!lines.equals("{\"status\":1}")) {
          System.out.println(info.getSubmId());
        }
      }
      reader.close();
      Thread.sleep(200);
    }
  }
}
