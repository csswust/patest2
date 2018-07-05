package com.csswust.patest2.service.impl;

import com.alibaba.fastjson.JSON;
import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamPaperService;
import com.csswust.patest2.service.OnlineUserService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.common.ConditionBuild;
import com.csswust.patest2.service.result.DrawProblemParam;
import com.csswust.patest2.utils.CipherUtil;
import com.csswust.patest2.vo.PersonExamPaper;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @Autowired
    private ConditionBuild conditionBuild;
    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private ResultInfoDao resultInfoDao;

    @Override
    public APIResult selectByCondition(ExamPaper examPaper, Boolean onlyPaper, Boolean containOnline, String userName, String studentNumber, Integer page, Integer rows) {
        APIResult apiResult = new APIResult();
        if (examPaper == null) {
            apiResult.setStatusAndDesc(-501, "examPaper不能为空");
        }
        BaseQuery baseQuery = new BaseQuery();
        conditionBuild.buildExamPaper(baseQuery, examPaper, userName, studentNumber);
        Integer total = examPaperDao.selectByConditionGetCount(examPaper, baseQuery);
        baseQuery.setPageRows(page, rows);
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, baseQuery);
        apiResult.setDataKey("total", total);
        apiResult.setDataKey("examPaperList", examPaperList);
        if (onlyPaper) {
            return apiResult;
        }
        List<Integer> userIds = getFieldByList(examPaperList, "userId", ExamPaper.class);
        List<UserInfo> userInfoList = selectRecordByIds(userIds,
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        List<ExamInfo> examInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "examId", ExamPaper.class),
                "examId", (BaseDao) examInfoDao, ExamInfo.class);
        if (containOnline) {
            List<String> sessinoList = onlineUserService.judgeOnline(userIds);
            apiResult.setDataKey("sessinoList", sessinoList);
        }
        apiResult.setDataKey("userInfoList", userInfoList);
        apiResult.setDataKey("userProfileList", userProfileList);
        apiResult.setDataKey("examInfoList", examInfoList);
        return apiResult;
    }

    private final class ExcelInfo {
        private String studentNumber;
        private String classRoom;
        private Integer userProfileId;
        private UserProfile userProfile;

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }

        public Integer getUserProfileId() {
            return userProfileId;
        }

        public void setUserProfileId(Integer userProfileId) {
            this.userProfileId = userProfileId;
        }

        public UserProfile getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(UserProfile userProfile) {
            this.userProfile = userProfile;
        }
    }

    private APIResult checkExcel(MultipartFile multipartFile, Integer examId) {
        APIResult apiResult = new APIResult();
        if (examId == null) {
            apiResult.setStatusAndDesc(-1, "考试Id不能为空");
            return apiResult;
        }
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            apiResult.setStatusAndDesc(-2, "当前考试不存在，可能已被删除");
            return apiResult;
        }
        Date date = new Date();
        if (date.getTime() > examInfo.getEndTime().getTime()) {
            apiResult.setStatusAndDesc(-3, "考试已结束，不能修改考试名单");
            return apiResult;
        }
        if (date.getTime() > examInfo.getStartTime().getTime()) {
            apiResult.setStatusAndDesc(-4, "考试进行中，不能修改考试名单");
            return apiResult;
        }
        if (multipartFile.isEmpty()) {
            apiResult.setStatusAndDesc(-5, "上传文件为空");
            return apiResult;
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
            apiResult.setStatusAndDesc(-6, "复制文件失败");
            return apiResult;
        }
        InputStream in;
        try {
            in = new FileInputStream(tempFile);
        } catch (Exception e) {
            log.error("new FileInputStream file: {} error: {}", tempFile.getPath(), e);
            apiResult.setStatusAndDesc(-7, "创建文件流失败");
            return apiResult;
        }
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(in);
        } catch (Exception e) {
            log.error("Workbook.getWorkbook file: {} error: {}", tempFile.getPath(), e);
            apiResult.setStatusAndDesc(-8, "解析excel失败");
            return apiResult;
        }
        List<ExcelInfo> excelInfoList = new ArrayList<>();
        List<String> numberDigitErrorList = new ArrayList<>();
        List<String> numberExistErrorList = new ArrayList<>();
        here:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                ExcelInfo excelInfo = new ExcelInfo();
                String studentNumber = sheet.getCell(0, j).getContents();
                String classRoom = sheet.getCell(1, j).getContents();
                excelInfo.setStudentNumber(studentNumber);
                excelInfo.setClassRoom(classRoom);
                excelInfoList.add(excelInfo);

                // 校验学号
                if (StringUtils.isBlank(studentNumber)) {
                    apiResult.setStatusAndDesc(-7, "学号不能为空");
                    break here;
                }
                if (studentNumber.length() < 4) {
                    if (!numberDigitErrorList.contains(studentNumber)) {
                        numberDigitErrorList.add(studentNumber);// 学号长度必须大于4
                    }
                    continue;
                }
                UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
                if (userProfile == null) {
                    if (!numberExistErrorList.contains(studentNumber)) {
                        numberExistErrorList.add(studentNumber);// 学号不存在
                    }
                    continue;
                }
                excelInfo.setUserProfileId(userProfile.getUseProId());
                excelInfo.setUserProfile(userProfile);
            }
        }
        if (apiResult.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
        }
        try {
            workbook.close();
            in.close();
        } catch (IOException e) {
            log.error("workbook.close file: {} error: {}", tempFile.getAbsoluteFile(), e);
        }
        if (apiResult.getStatus() == 0) {
            apiResult.setDataKey("excelInfoList", excelInfoList);
            apiResult.setDataKey("numberDigitErrorList", numberDigitErrorList);
            apiResult.setDataKey("numberExistErrorList", numberExistErrorList);
            apiResult.setStatusAndDesc(1, "解析成功");
        }
        return apiResult;
    }

    @Transactional
    @Override
    public APIResult insertByExcel(MultipartFile multipartFile, Integer examId, boolean isIgnoreError) {
        APIResult apiResult = new APIResult();
        APIResult checkResult = checkExcel(multipartFile, examId);
        if (checkResult.getStatus() != 1) {
            apiResult.setStatusAndDesc(checkResult.getStatus(), checkResult.getDesc());
            return apiResult;
        }
        List<ExcelInfo> excelInfoList = (List<ExcelInfo>) checkResult.getData().get("excelInfoList");
        List<String> numberDigitErrorList = (List<String>) checkResult.getData().get("numberDigitErrorList");
        List<String> numberExistErrorList = (List<String>) checkResult.getData().get("numberExistErrorList");
        boolean flagTemp = false;
        if (numberDigitErrorList.size() != 0) {
            flagTemp = true;
            apiResult.setDataKey("numberDigitErrorList", numberDigitErrorList);
        }
        if (numberExistErrorList.size() != 0) {
            flagTemp = true;
            apiResult.setDataKey("numberExistErrorList", numberExistErrorList);
        }
        if (flagTemp) {
            apiResult.setStatusAndDesc(-200, "学号位数错误或者不存在错误");
            return apiResult;
        }

        List<UserInfo> userInfoList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        List<ExamPaper> examPaperList = new ArrayList<>();

        Random random = new Random();
        int count = 0, flag = getLastByExam(examId);
        String prefixStr = getUsernamePrefix(examId);
        for (ExcelInfo excelInfo : excelInfoList) {
            String studentNumber = excelInfo.getStudentNumber();
            Integer userProfileId = excelInfo.getUserProfileId();
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(getUsername(prefixStr, flag, studentNumber));
            userInfo.setUserProfileId(userProfileId);
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
                apiResult.setStatusAndDesc(-11, JSON.toJSONString(userInfo) + "插入失败");
                break;
            }
            ExamPaper examPaper = new ExamPaper();
            examPaper.setExamId(examId);
            examPaper.setExamId(examId);
            examPaper.setUserId(userInfo.getUserId());
            examPaper.setClassroom(excelInfo.getClassRoom());
            examPaper.setAcedCount(0);
            examPaper.setUsedTime(0);
            examPaper.setIsMarked(0);
            examPaper.setScore(0.0);
            temp = examPaperDao.insertSelective(examPaper);
            if (temp != 1) {
                apiResult.setStatusAndDesc(-12, JSON.toJSONString(examPaper) + "插入失败");
                break;
            }
            userInfoList.add(userInfo);
            passwordList.add(StringBuilder.toString());
            examPaperList.add(examPaper);
            count = count + 1;
            flag = flag + 1;
        }
        if (apiResult.getStatus() != 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
        }

        String fname = "loginsheet" + new Date().getTime() + ".xls";
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE);
        File downFile = new File(path, fname);
        WritableWorkbook work = null;
        try {
            work = Workbook.createWorkbook(downFile);
        } catch (IOException e) {
            apiResult.setStatusAndDesc(-13, "创建WritableWorkbook失败");
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
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
                sheet.addCell(new Label(1, i + 1, excelInfoList.get(i).getStudentNumber()));
                sheet.addCell(new Label(2, i + 1, excelInfoList.get(i).getUserProfile().getRealName()));
                sheet.addCell(new Label(3, i + 1, userInfoList.get(i).getUsername()));
                sheet.addCell(new Label(4, i + 1, passwordList.get(i)));
                sheet.addCell(new Label(5, i + 1, examPaperList.get(i).getClassroom()));
            }
        } catch (WriteException e) {
            apiResult.setStatusAndDesc(-14, "构建sheet出现异常：" + e.getMessage());
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
        }
        // 把创建的内容写入到输出流中，并关闭输出流
        try {
            work.write();
            work.close();
        } catch (Exception e) {
            apiResult.setStatusAndDesc(-15, "写入excel出现异常：" + e.getMessage());
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
        }
        ExamInfo record = new ExamInfo();
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        examInfo.setExamId(examId);
        examInfoDao.updateByPrimaryKeySelective(record);
        apiResult.setDataKey("dirPath", downFile.getPath());
        apiResult.setStatusAndDesc(count, "导入成功");
        return apiResult;
    }

    @Transactional
    @Override
    public APIResult drawProblemByExamId(Integer examId, Integer userId) {
        APIResult apiResult = new APIResult();
        Random random = new Random();
        int i, j;
        // 获取试卷列表
        ExamPaper examPaper = new ExamPaper();
        examPaper.setExamId(examId);
        if (userId != null) examPaper.setUserId(userId);// 对单个用户抽题
        List<ExamPaper> examPaperList = examPaperDao.selectByCondition(examPaper, new BaseQuery());
        if (examPaperList == null || examPaperList.size() == 0) {
            apiResult.setStatusAndDesc(-1, "未添加考生");
            return apiResult;
        }
        if (userId != null && (examPaperList.size() != 1
                || examPaperList.get(0).getUserId().intValue() != userId.intValue())) {
            apiResult.setStatusAndDesc(-500, "发生了未知异常");
            return apiResult;
        }
        // 获取试卷参数
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);
        List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery());
        if (examParamList == null || examParamList.size() == 0) {
            apiResult.setStatusAndDesc(-2, "未添加试卷参数");
            return apiResult;
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
                apiResult.setStatusAndDesc(-3, JSON.toJSONString(examParamList.get(i)) +
                        "试卷参数对应的题目数目为0");
                return apiResult;
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
                    apiResult.setStatusAndDesc(-4,
                            "试卷参数与题目数目异常，请检查重复参数是否小于或者等于对应的题目数目");
                    return apiResult;
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
            apiResult.setStatusAndDesc(-100, "插入失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return apiResult;
        }
        if (userId == null) {
            ExamInfo record = new ExamInfo();
            record.setExamId(examId);
            record.setIsDrawProblem(1);
            examInfoDao.updateByPrimaryKeySelective(record);
        }
        apiResult.setStatusAndDesc(sum, "抽题成功");
        return apiResult;
    }

    @Transactional
    @Override
    public APIResult insertOne(Integer examId, String studentNumber, String password) {
        APIResult apiResult = new APIResult();
        if (examId == null || StringUtils.isBlank(studentNumber)
                || StringUtils.isBlank(password)) {
            apiResult.setStatusAndDesc(-501, "参数不合法");
            return apiResult;
        }
        UserProfile userProfile = userProfileDao.selectByStudentNumber(studentNumber);
        if (userProfile == null) {
            apiResult.setStatusAndDesc(-1, "学号不存在");
            return apiResult;
        }
        Integer userProfileId = userProfile.getUseProId();
        UserInfo userInfo = new UserInfo();
        int flag = getLastByExam(examId);
        String prefixStr = getUsernamePrefix(examId);
        userInfo.setUsername(getUsername(prefixStr, flag, studentNumber));
        userInfo.setUserProfileId(userProfileId);
        userInfo.setPassword(password);
        userInfo.setIsAdmin(0);
        userInfo.setIsTeacher(0);
        userInfo.setExamId(examId);
        userInfo.setIsActive(1);
        int temp = userInfoDao.insertSelective(userInfo);
        if (temp != 1) {
            apiResult.setStatusAndDesc(-11, JSON.toJSONString(userInfo) + "插入失败");
            return apiResult;
        }
        ExamPaper examPaper = new ExamPaper();
        examPaper.setExamId(examId);
        examPaper.setUserId(userInfo.getUserId());
        examPaper.setAcedCount(0);
        examPaper.setScore(0.0);
        examPaper.setIsMarked(0);
        int status1 = examPaperDao.insertSelective(examPaper);
        UserInfo record = new UserInfo();
        record.setUserId(userInfo.getUserId());
        record.setExamId(examId);
        int status2 = userInfoDao.updateByPrimaryKeySelective(record);
        if (status1 != 1 || status2 != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            apiResult.setStatusAndDesc(-2, format("插入失败：examID: %d,%s", examId, studentNumber));
        } else {
            apiResult.setStatusAndDesc(1, "添加成功");
        }
        return apiResult;
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

    private int getLastByExam(int examId) {
        if (examId == 0) return 1;
        UserInfo userCondition = new UserInfo();
        userCondition.setExamId(examId);
        int index = userInfoDao.selectByConditionGetCount(userCondition, new BaseQuery());
        int result = index + 1;
        List<UserInfo> userInfoList = userInfoDao.selectByCondition(userCondition, new BaseQuery(1, 1));
        if (userInfoList != null && userInfoList.size() != 0) {
            UserInfo userInfo = userInfoList.get(0);
            if (userInfo == null) return result;
            String username = userInfo.getUsername();
            if (StringUtils.isBlank(username)) return result;
            if (username.length() < 13) return result;
            StringBuilder builder = new StringBuilder();
            for (int i = 5; i < username.length() - 4; i++) {
                builder.append(username.charAt(i));
            }
            Integer toInt = StringToInt(builder.toString());
            if (toInt == null) return result;
            return Math.max(toInt + 1, result);
        }
        return result;
    }

    private String getUsernamePrefix(int examId) {
        String examYear;  // 获取年份
        Calendar now = Calendar.getInstance();
        examYear = String.format("%02d", (now.get(Calendar.YEAR)) % 2000);
        String examIdFormat = String.format("%03d", examId % 1000);
        return examYear + examIdFormat;
    }

    private String getUsername(String prefix, int flag, String studentNumber) {
        String indexString = null;
        if (flag >= 10000) indexString = String.valueOf(flag);
        else indexString = String.format("%04d", flag % 10000);
        int lenth = studentNumber.length();
        String numberString = studentNumber.substring(lenth - 4, lenth);
        return prefix + indexString + numberString;
    }

    @Override
    public File exportInfoByExamId(Integer examId) throws IOException {
        Config.refreshConfig();
        String tempPath = Config.get("examPaperTem");
        //下载文件路径
        File path = new File(tempPath + "/" + System.currentTimeMillis());
        if (!path.exists())
            path.mkdirs();
        List<PersonExamPaper> classPaper = getClassPaperScore(examId);
        ExamInfo examInfo = classPaper.get(0).getExamInfo();
        File zip = new File(path + "/" + examInfo.getTitle() + "试卷成绩.zip");
        zip.createNewFile();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
        for (PersonExamPaper person : classPaper) {
            UserProfile userProfile = person.getUserProfile();
            File temp = new File(path + "/" + userProfile.getRealName() + "-" + userProfile.getStudentNumber() + ".html");
            temp.createNewFile();
            OutputStream outputStream = new FileOutputStream(temp);
            printPerson(person, outputStream);
            outputStream.close();
            byte[] buf = new byte[1024];
            zos.putNextEntry(new ZipEntry(temp.getName()));
            int len;
            FileInputStream in = new FileInputStream(temp);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
            temp.delete();
        }
        zos.close();
        return zip;
    }

    private void printPerson(PersonExamPaper person, OutputStream outputStream) throws UnsupportedEncodingException {
        ExamPaper examPaper = person.getExamPaper();
        UserProfile userProfile = person.getUserProfile();
        //获取总分
        int total = 0;
        for (Integer integer : person.getAllScore()) {
            total += integer;
        }
        PrintWriter print = new PrintWriter(new OutputStreamWriter(outputStream, "utf-8"));
        print.println("<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "</head>\n");
        print.println("<h1>");
        print.println(person.getExamInfo().getTitle());
        print.println("</h1>");
        print.println("<h1>");
        print.println(userProfile.getRealName() + "-" + userProfile.getClassName()
                + "  Ac:" + examPaper.getAcedCount() + " / " + person.getProblemInfos().size()
                + "  得分：" + examPaper.getScore() + "/" + total);
        print.println("</h1>");
        print.println("<br>");
        int order = 0;
        for (ProblemInfo problemInfo : person.getProblemInfos()) {
            print.println("<h2>-------------题目 " + (char) ('A' + order) + " " + problemInfo.getTitle() + "  ----------</h2>");
            print.println("<br>");
            print.println("<span style=\"font-weight:700\">结果</span> ：" + "<span style=\"color:green\">" + person.getStatus().get(order) + "</span>");
            print.println("<span style=\"font-weight:700\">得分</span> ：" + "<span style=\"color:green\">" + person.getScore().get(order) + " / " + person.getAllScore().get(order++)+ "</span>");
            print.println("<br>");
            print.println("<p>");
            print.println(problemInfo.getDescription());
            print.println("</p>");
            print.println("Memory Limit(mb): " + problemInfo.getMemoryLimit() + "<br>Time Limit(ms) : " + problemInfo.getTimeLimit());
            print.println("<br>");
            print.println("<span style=\"font-weight:700\">输入</span> :" + problemInfo.getInputTip());
            print.println("<br>");
            print.println("<span style=\"font-weight:700\">输出</span> :" + problemInfo.getOutputTip());
            print.println("<br>");
            print.println("<span style=\"font-weight:700\">样例输入</span> ：<div style=\"font-size:15px;color:#666\">" + problemInfo.getInputSample() + "</div>");
            print.println("<span style=\"font-weight:700\">样例输出</span> : " + problemInfo.getOutputSample());
            print.println("<br>");
            print.println("<br>");
        }
        print.println("</html>");
        print.close();
    }

    @Override
    public List<PersonExamPaper> getClassPaperScore(Integer examId) {
        //结果集合
        Map<Integer, String> results = getResultInfos();
        //参数集
        Map<Integer, Integer> params = getScores(examId);
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        //获取所有的考号
        ExamPaper examPaperQuery = new ExamPaper();
        examPaperQuery.setExamId(examId);
        List<ExamPaper> examPapers = examPaperDao.selectByCondition(examPaperQuery, new BaseQuery());
        //获取所有用户信息存到<id,UserProfile>中
        Map<Integer, Integer> proIdAndUserId = new HashMap<>(examPapers.size());
        List<Integer> userids = new ArrayList<>(examPapers.size());
        for (ExamPaper userInfo : examPapers) {
            userids.add(userInfo.getUserId());
        }
        List<UserInfo> userInfos = userInfoDao.selectByIdsList(userids);
        List<Integer> userproids = new ArrayList<>(examPapers.size());
        for (UserInfo userInfo : userInfos) {
            userproids.add(userInfo.getUserProfileId());
            proIdAndUserId.put(userInfo.getUserProfileId(), userInfo.getUserId());
        }
        List<UserProfile> userProfiles = userProfileDao.selectByIdsList(userproids);
        Map<Integer, UserProfile> userProfileMap = new HashMap<>(userProfiles.size());
        for (UserProfile userProfile : userProfiles) {
            userProfileMap.put(proIdAndUserId.get(userProfile.getUseProId()), userProfile);
        }
        //所有的成绩单
        List<PersonExamPaper> classPaper = new ArrayList<>(examPapers.size());
        for (ExamPaper examPaper : examPapers) {
            //获取该生的所有题目
            PaperProblem paperProblem = new PaperProblem();
            paperProblem.setExamPaperId(examPaper.getExaPapId());
            paperProblem.setExamId(examId);
            List<PaperProblem> problems = paperProblemDao.selectByCondition(paperProblem, new BaseQuery());
            //获取所有题目详情
            List<ProblemInfo> problemInfos = new ArrayList<>(problems.size());
            //所有题目的结果
            List<String> status = new ArrayList<>(problems.size());
            List<Integer> allScore = new ArrayList<>(problems.size());
            List<Double> scores = new ArrayList<>(problems.size());
            for (PaperProblem problem : problems) {
                ProblemInfo tem = problemInfoDao.selectByPrimaryKey(problem.getProblemId());
                problemInfos.add(tem);
                status.add(results.get(problem.getIsAced()));
                allScore.add(params.get(tem.getLevelId()));
                scores.add(problem.getScore());
            }
            PersonExamPaper personExamPaper = new PersonExamPaper(
                    examInfo
                    , userProfileMap.get(examPaper.getUserId())
                    , examPaper
                    , problemInfos
                    , status
                    ,allScore
                    ,scores);
            classPaper.add(personExamPaper);
        }
        return classPaper;
    }

    private Map<Integer, String> getResultInfos() {
        List<ResultInfo> infos = resultInfoDao.selectByCondition(new ResultInfo(),new BaseQuery());
        Map<Integer, String> map = new HashMap<>(infos.size());
        for (ResultInfo result : infos) {
            map.put(result.getResuId(),result.getName());
        }
        map.put(0,"未做");
        return map;
    }

    private Map<Integer,Integer> getScores(Integer examId) {
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);
        List<ExamParam> examParams = examParamDao.selectByCondition(examParam,new BaseQuery());
        Map<Integer, Integer> map = new HashMap<>(examParams.size());
        for (ExamParam parm : examParams) {
                map.put(parm.getLevelId(),parm.getScore());
        }
        return map;
    }
}
