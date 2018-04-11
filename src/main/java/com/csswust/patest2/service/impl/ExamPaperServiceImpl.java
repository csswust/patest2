package com.csswust.patest2.service.impl;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.DrawProblemParam;
import com.csswust.patest2.service.result.DrawProblemRe;
import com.csswust.patest2.service.result.ExamPaperLoadRe;
import com.csswust.patest2.utils.CipherUtil;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

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
    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private ExamProblemDao examProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private ExamInfoDao examInfoDao;

    @Transactional
    @Override
    public ExamPaperLoadRe insertByExcel(MultipartFile multipartFile, Integer examId, boolean isIgnoreError) {
        ExamPaperLoadRe result = new ExamPaperLoadRe();
        if (examId == null) {
            result.setStatus(-1);
            result.setDesc("考试Id不能为空");
            return result;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            result.setStatus(-2);
            result.setDesc("当前考试不存在，可能已被删除");
            return result;
        }
        Date date = new Date();
        if (date.getTime() > examInfo.getEndTime().getTime()) {
            result.setStatus(-3);
            result.setDesc("考试已结束，不能修改考试名单");
            return result;
        }
        if (date.getTime() > examInfo.getStartTime().getTime()) {
            result.setStatus(-4);
            result.setDesc("考试进行中，不能修改考试名单");
            return result;
        }
        if (multipartFile.isEmpty()) {
            result.setStatus(-5);
            result.setDesc("上传文件为空");
            return result;
        }
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE);
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
            result.setStatus(-6);
            result.setDesc("复制文件失败");
            return result;
        }
        InputStream in;
        try {
            in = new FileInputStream(tempFile);
        } catch (Exception e) {
            log.error("new FileInputStream file: {} error: {}", tempFile.getPath(), e);
            result.setStatus(-7);
            result.setDesc("创建文件流失败");
            return result;
        }
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(in);
        } catch (Exception e) {
            log.error("Workbook.getWorkbook file: {} error: {}", tempFile.getPath(), e);
            result.setStatus(-8);
            result.setDesc("解析excel失败");
            return result;
        }
        // 删除原本的数据
        // int userInfoDelete = userInfoDao.deleteByExamId(examId);
        UserInfo userCondition = new UserInfo();
        userCondition.setExamId(examId);
        int index = userInfoDao.selectByConditionGetCount(userCondition, new BaseQuery());
        int examPaperDelete = examPaperDao.deleteByExamId(examId);
        String examYear;  // 获取年份
        Calendar now = Calendar.getInstance();
        examYear = String.format("%02d", (now.get(Calendar.YEAR)) % 2000);
        String examIdFormat = String.format("%03d", examId % 1000);

        List<UserProfile> userProfileList = new ArrayList<>();
        List<UserInfo> userInfoList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        List<ExamPaper> examPaperList = new ArrayList<>();
        Random random = new Random();
        int count = 0, flag = index + 1;
        here:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                String studentNumber = sheet.getCell(0, j).getContents();
                if (studentNumber == null || studentNumber.length() < 4) {
                    result.setStatus(-9);
                    result.setUserNameError(studentNumber);
                    result.setDesc("学号长度必须大于4");
                    break here;
                }
                String classRoom = sheet.getCell(1, j).getContents();
                UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
                if (userProfile == null) {
                    result.setStatus(-10);
                    result.setUserNameError(studentNumber);
                    result.setDesc(studentNumber + "学号不存在信息");
                    break here;
                }
                UserInfo userInfo = new UserInfo();
                String indexString = null;
                if (flag >= 10000) indexString = String.valueOf(flag);
                else indexString = String.format("%04d", flag % 10000);

                int lenth = studentNumber.length();
                String numberString = studentNumber.substring(lenth - 4, lenth);
                userInfo.setUsername(examYear + examIdFormat + indexString + numberString);
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
                    result.setStatus(-11);
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
                examPaper.setScore(0.0);
                temp = examPaperDao.insertSelective(examPaper);
                if (temp != 1) {
                    result.setStatus(-12);
                    result.setUserNameError(studentNumber);
                    result.setDesc(JSON.toJSONString(examPaper) + "插入失败");
                    break here;
                }
                userProfileList.add(userProfile);
                userInfoList.add(userInfo);
                passwordList.add(StringBuilder.toString());
                examPaperList.add(examPaper);
                count = count + 1;
                flag = flag + 1;
            }
        }
        if (result.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        String fname = "loginsheet" + new Date().getTime() + ".xls";
        File downFile = new File(path, fname);
        WritableWorkbook work = null;
        try {
            work = Workbook.createWorkbook(downFile);
        } catch (IOException e) {
            result.setStatus(-13);
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
            result.setStatus(-14);
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
            result.setStatus(-15);
            result.setDesc("写入excel出现异常：" + e.getMessage());
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        result.setDirPath(downFile.getPath());
        result.setStatus(count);
        return result;
    }

    @Transactional
    @Override
    public DrawProblemRe drawProblemByExamId(Integer examId, Integer userId) {
        DrawProblemRe drawProblemRe = new DrawProblemRe();
        Random random = new Random();
        int i, j;
        // 获取试卷列表
        ExamPaper examPaper = new ExamPaper();
        examPaper.setExamId(examId);
        if (userId != null) examPaper.setUserId(userId);// 对单个用户抽题
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, new BaseQuery());
        if (examPaperList == null || examPaperList.size() == 0) {
            drawProblemRe.setStatus(-1);
            drawProblemRe.setDesc("未添加考生");
            return drawProblemRe;
        }
        if (userId != null && (examPaperList.size() != 1
                || examPaperList.get(0).getUserId().intValue() != userId.intValue())) {
            drawProblemRe.setStatus(-500);
            drawProblemRe.setDesc("发生了未知异常");
            return drawProblemRe;
        }
        // 获取试卷参数
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);
        List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery());
        if (examParamList == null || examParamList.size() == 0) {
            drawProblemRe.setStatus(-2);
            drawProblemRe.setDesc("未添加试卷参数");
            return drawProblemRe;
        }
        // 包装模板和对应的问题，目的在于优化效率
        List<DrawProblemParam> dPPList = new ArrayList<>();
        for (i = 0; i < examParamList.size(); i++) {
            DrawProblemParam dpp = new DrawProblemParam();
            BaseQuery baseQuery = new BaseQuery();
            baseQuery.setCustom("examId", examId);
            baseQuery.setCustom("knowId", examParamList.get(i).getKnowId());
            baseQuery.setCustom("levelId", examParamList.get(i).getLevelId());
            List<ExamProblem> examProblemList = examProblemDao.selectByProblem(
                    new ExamProblem(), baseQuery);
            if (examProblemList == null || examProblemList.size() == 0) {
                drawProblemRe.setStatus(-3);
                drawProblemRe.setDesc(JSON.toJSONString(examParamList.get(i)) +
                        "试卷参数对应的题目数目为0");
                return drawProblemRe;
            }
            dpp.setExamProblemList(examProblemList);
            List<ProblemInfo> problemInfoList = selectRecordByIds(
                    getFieldByList(examProblemList, "problemId", ExamProblem.class),
                    "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
            dpp.setProblemInfoList(problemInfoList);
            dPPList.add(dpp);
        }
        // 对每张试卷进行抽题
        List<PaperProblem> pPList = new ArrayList<>();
        for (i = 0; i < examPaperList.size(); i++) { // 对每张试卷，也就是每个用户
            // 保证不重复
            Set<Integer> setProbId = new HashSet<>();
            for (j = 0; j < dPPList.size(); j++) { // 对每个模板
                int probNum = dPPList.get(j).getExamProblemList().size();
                int result = random.nextInt(probNum);
                boolean flag = false;
                for (int k = result; k < result + probNum; k++) {
                    int probId = dPPList.get(j).getExamProblemList().get(k % probNum).getProblemId();
                    if (!setProbId.contains(probId)) {
                        setProbId.add(probId);
                        PaperProblem paperProblem = new PaperProblem();
                        paperProblem.setExamId(examId);
                        paperProblem.setExamPaperId(examPaperList.get(i).getExaPapId());
                        paperProblem.setOrder(j);
                        paperProblem.setExamParamId(examParamList.get(j).getExaParId());
                        paperProblem.setProblemId(probId);
                        paperProblem.setIsAced(0);
                        paperProblem.setScore(0.0);
                        paperProblem.setSubmitCount(0);
                        paperProblem.setUsedTime(0);
                        pPList.add(paperProblem);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    drawProblemRe.setStatus(-4);
                    drawProblemRe.setDesc("试卷参数与题目数目异常，请检查重复参数是否小于或者等于对应的题目数目");
                    return drawProblemRe;
                }
            }
        }
        // 删除之前抽的题目
        int paperProbelmDelete = 0;
        if (userId == null) paperProbelmDelete = paperProblemDao.deleteByExamId(examId);
        else {
            PaperProblem paperProblem = new PaperProblem();
            paperProblem.setExamPaperId(examPaperList.get(0).getExaPapId());
            List<PaperProblem> paperProblemList = paperProblemDao.selectByCondition(
                    paperProblem, new BaseQuery());
            for (PaperProblem item : paperProblemList) {
                paperProbelmDelete += paperProblemDao.deleteByPrimaryKey(item.getPapProId());
            }
        }
        // 添加新抽的题目,改为批量插入，优化效率
        int sum = paperProblemDao.insertBatch(pPList);
        if (sum != pPList.size()) {
            drawProblemRe.setDesc("插入失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return drawProblemRe;
        }
        /*for (i = 0; i < pPList.size(); i++) {
            int temp = paperProblemDao.insertSelective(pPList.get(i));
            if (temp != 1) {
                drawProblemRe.setStatus(-5);
                drawProblemRe.setDesc(JSON.toJSONString(pPList.get(i)) + "插入失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return drawProblemRe;
            }
            sum = sum + temp;
        }*/
        drawProblemRe.setStatus(sum);
        return drawProblemRe;
    }

    private String getPassword(String pass) {
        //设置密码
        try {
            return CipherUtil.encode(pass);
        } catch (Exception e) {
            log.error("CipherUtil.encode({}) error: {}", pass, e);
        }
        return null;
    }
}
