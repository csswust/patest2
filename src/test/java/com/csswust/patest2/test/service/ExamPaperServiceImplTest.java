package com.csswust.patest2.test.service;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.entity.ExamInfo;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import com.csswust.patest2.vo.PersonExamPaper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Create by Wzy
 * on 2018/7/4 19:52
 * 不短不长八字刚好
 */
public class ExamPaperServiceImplTest extends JunitBaseServiceDaoTest {

    @Autowired
    private ExamPaperService examPaperService;

    @Test
    public void exportInfoByExamId() throws IOException {
//        System.out.println(examPaperService.exportInfoByExamId(19));
//        File temp = new File("G:\\temp.html");
//        temp.createNewFile();
//        OutputStream outputStream = new FileOutputStream(temp);
//        PrintWriter print = new PrintWriter(outputStream);
//        print.println("asdfasdf");
//        print.close();
//        outputStream.close();
//        List<PersonExamPaper> classPaper = examPaperService.getClassPaperScore(17);
//        ExamInfo examInfo = classPaper.get(0).getExamInfo();
//        for (PersonExamPaper person : classPaper) {
//            ExamPaper examPaper = person.getExamPaper();
//            UserProfile userProfile = person.getUserProfile();
//            File temp = new File( "G:\\" + userProfile.getRealName() + "-" + examInfo.getTitle()+ ".html");
//            temp.createNewFile();
//            OutputStream outputStream = new FileOutputStream(temp);
//            PrintWriter print = new PrintWriter(outputStream);
//            print.println("<!DOCTYPE html>\n" +
//                    "<html lang=\"zh-CN\">\n" +
//                    "<head>\n" +
//                    "    <meta charset=\"utf-8\">\n" +
//                    "</head>\n");
//            print.println(userProfile.getRealName() + "-" + userProfile.getClassName()
//                    + "  Ac:" + examPaper.getAcedCount() + "  得分：" + examPaper.getScore());
//            print.println("<br>");
//            print.println("<br>");
//            int order = 0;
//            for (ProblemInfo problemInfo:person.getProblemInfos()) {
//                print.println("-------------题目 " + (char)('A' + order) + " " + problemInfo.getTitle() + "  ----------");
//                print.println("<br>");
//                print.println(" 是否AC ： " + person.getIsAc().get(order++));
//                print.println("<br>");
//                print.println(problemInfo.getTitle());
//                print.println("<br>");
//                print.println(problemInfo.getDescription());
//                print.println("<br>");
//                print.println("Memory Limit(mb): " + problemInfo.getMemoryLimit() + "Time Limit(ms) : " );
//                print.println("<br>");
//                print.println("输入：" + problemInfo.getInputTip());
//                print.println("<br>");
//                print.println("输出：" + problemInfo.getOutputTip());
//                print.println("<br>");
//                print.println("样例输入：" + problemInfo.getInputSample());
//                print.println("<br>");
//                print.println("样例输出：" + problemInfo.getOutputSample());
//                print.println("<br>");
//                print.println("<br>");
//            }
//            print.println("</html>");
//            print.close();
//            outputStream.close();
//        }
        examPaperService.exportInfoByExamId(19);
    }

    @Test
    public void test() throws IOException {
        File file = new File("G:\\temp.zip");
        file.createNewFile();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));

        byte[] buf = new byte[256];
        File file1 = new File("G:\\11.txt");
        zos.putNextEntry(new ZipEntry(file1.getName()));
        int len;
        FileInputStream in = new FileInputStream(file1);
        while ((len = in.read(buf)) != -1){
            zos.write(buf, 0, len);
        }
        zos.closeEntry();
        in.close();
        zos.close();
    }
}
