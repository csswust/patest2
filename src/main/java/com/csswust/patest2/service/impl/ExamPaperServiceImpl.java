package com.csswust.patest2.service.impl;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.ExamPaperDao;
import com.csswust.patest2.dao.UserInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.entity.ExamPaper;
import com.csswust.patest2.entity.UserInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.ExamPaperLoadRe;
import com.csswust.patest2.utils.MD5Util;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by 972536780 on 2018/3/21.
 */
@Service
public class ExamPaperServiceImpl extends BaseService implements ExamPaperService {
    private static Logger log = LoggerFactory.getLogger(ExamPaperServiceImpl.class);

    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;

    @Transactional
    @Override
    public ExamPaperLoadRe insertByExcel(MultipartFile multipartFile, Integer examId, boolean isIgnoreError) {
        ExamPaperLoadRe result = new ExamPaperLoadRe();
        if (examId == null) {
            result.setStatus(-1);
            result.setDesc("考试Id不能为空");
            return result;
        }
        if (multipartFile.isEmpty()) {
            result.setStatus(-2);
            result.setDesc("上传文件为空");
            return result;
        }
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR);
        String filename = multipartFile.getOriginalFilename() + (new Date().getTime());
        File filepath = new File(path, filename);
        //判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        File tempFile = new File(path + File.separator + filename);
        try {
            multipartFile.transferTo(tempFile);
        } catch (Exception e) {
            log.error("multipartFile.transferTo file: {} error: {}",
                    tempFile.getAbsoluteFile(), e);
            result.setStatus(-3);
            result.setDesc("复制文件失败");
            return result;
        }
        InputStream in;
        try {
            in = new FileInputStream(tempFile);
        } catch (Exception e) {
            log.error("new FileInputStream file: {} error: {}", tempFile.getPath(), e);
            result.setStatus(-4);
            result.setDesc("创建文件流失败");
            return result;
        }
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(in);
        } catch (Exception e) {
            log.error("Workbook.getWorkbook file: {} error: {}", tempFile.getPath(), e);
            result.setStatus(-5);
            result.setDesc("解析excel失败");
            return result;
        }
        // 删除原本的数据
        int userInfoDelete = userInfoDao.deleteByExamId(examId);
        int examPaperDelete = examPaperDao.deleteByExamId(examId);
        String examYear;  // 获取年份
        Calendar now = Calendar.getInstance();
        examYear = String.format("%d", now.get(Calendar.YEAR));
        String examIdFormat = String.format("%04d", examId);

        List<UserProfile> userProfileList = new ArrayList<>();
        List<UserInfo> userInfoList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        List<ExamPaper> examPaperList = new ArrayList<>();
        Random random = new Random();
        int count = 0, flag = 0;
        here:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                String studentNumber = sheet.getCell(0, j).getContents();
                String classRoom = sheet.getCell(1, j).getContents();
                UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
                if (userProfile == null) {
                    result.setStatus(-6);
                    result.setUserNameError(studentNumber);
                    result.setDesc(studentNumber + "学号不存在信息");
                    break here;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(examYear + examIdFormat + studentNumber);
                userInfo.setUserProfileId(userProfile.getUseProId());
                StringBuilder StringBuilder = new StringBuilder();
                for (int l = 0; l < 8; l++) {
                    StringBuilder.append(random.nextInt(9));
                }
                userInfo.setPassword(getPassword(StringBuilder.toString()));
                userInfo.setIsAdmin(0);
                userInfo.setIsTeacher(0);
                userInfo.setExamId(examId);
                userInfo.setIsActive(1);
                int temp = userInfoDao.insertSelective(userInfo);
                if (temp != 1) {
                    result.setStatus(-7);
                    result.setUserNameError(studentNumber);
                    result.setDesc(JSON.toJSONString(userInfo) + "插入失败");
                    break here;
                }
                ExamPaper examPaper = new ExamPaper();
                examPaper.setExamId(examId);
                examPaper.setExamId(examId);
                examPaper.setUserId(userInfo.getUserId());
                examPaper.setClassroom(classRoom);
                examPaper.setAcedCount(0);
                examPaper.setUsedTime(0);
                examPaper.setIsMarked(0);
                examPaper.setScore(0);
                temp = examPaperDao.insertSelective(examPaper);
                if (temp != 1) {
                    result.setStatus(-8);
                    result.setUserNameError(studentNumber);
                    result.setDesc(JSON.toJSONString(examPaper) + "插入失败");
                    break here;
                }
                userProfileList.add(userProfile);
                userInfoList.add(userInfo);
                passwordList.add(StringBuilder.toString());
                examPaperList.add(examPaper);
                count = count + 1;
            }
        }
        if (result.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        String fname = "loginsheet" + new Date().getTime() + ".xml";
        File downFile = new File(path, fname);
        WritableWorkbook work = null;
        try {
            work = Workbook.createWorkbook(downFile);
        } catch (IOException e) {
            result.setStatus(-9);
            result.setDesc("创建WritableWorkbook失败");
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        try {
            // 创建新的一页
            WritableSheet sheet = work.createSheet("First Sheet", 0);
            sheet.addCell(new Label(0, 0, "编号"));
            sheet.addCell(new Label(1, 0, "学号"));
            sheet.addCell(new Label(2, 0, "姓名"));
            sheet.addCell(new Label(3, 0, "用户名"));
            sheet.addCell(new Label(4, 0, "密码"));
            sheet.addCell(new Label(5, 0, "考场"));
            for (int i = 0; i < count; i++) {
                sheet.addCell(new Label(0, i + 1, String.valueOf(i + 1)));
                sheet.addCell(new Label(1, i + 1, userProfileList.get(i).getStudentNumber()));
                sheet.addCell(new Label(2, i + 1, userProfileList.get(i).getRealName()));
                sheet.addCell(new Label(3, i + 1, userInfoList.get(i).getUsername()));
                sheet.addCell(new Label(4, i + 1, passwordList.get(i)));
                sheet.addCell(new Label(5, i + 1, examPaperList.get(i).getClassroom()));
            }
        } catch (WriteException e) {
            result.setStatus(-10);
            result.setDesc("构建sheet出现异常：" + e.getMessage());
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        // 把创建的内容写入到输出流中，并关闭输出流
        try {
            work.write();
            work.close();
        } catch (Exception e) {
            result.setStatus(-11);
            result.setDesc("写入excel出现异常：" + e.getMessage());
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        result.setDirPath(path);
        result.setFileName(fname);
        result.setStatus(count);
        return result;
    }

    private String getPassword(String pass) {
        //设置密码
        try {
            return MD5Util.encode(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
