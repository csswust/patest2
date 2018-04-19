package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.cache.SiteCache;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.*;
import com.csswust.patest2.dao.common.BaseDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.result.SelectProblemNumRe;
import com.csswust.patest2.entity.*;
import com.csswust.patest2.service.ExamInfoService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.ImportDataRe;
import com.csswust.patest2.utils.CipherUtil;
import com.csswust.patest2.utils.ZipUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

import static com.csswust.patest2.service.common.BatchQueryService.getFieldByList;
import static com.csswust.patest2.service.common.BatchQueryService.selectRecordByIds;

/**
 * Created by 972536780 on 2018/3/23.
 */
@Service
public class ExamInfoServiceImpl extends BaseService implements ExamInfoService {
    private static Logger log = LoggerFactory.getLogger(ExamInfoServiceImpl.class);

    @Autowired
    private ExamInfoDao examInfoDao;
    @Autowired
    private ExamPaperDao examPaperDao;
    @Autowired
    private ExamParamDao examParamDao;
    @Autowired
    private PaperProblemDao paperProblemDao;
    @Autowired
    private ProblemInfoDao problemInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private ResultInfoDao resultInfoDao;
    @Autowired
    private UserLoginLogDao userLoginLogDao;
    @Autowired
    private SubmitInfoDao submitInfoDao;
    @Autowired
    private JudgerInfoDao judgerInfoDao;

    @Override
    public ImportDataRe importCodeByExamId(Integer examId) {
        ImportDataRe result = new ImportDataRe();
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examId == null || examInfo == null) {
            result.setStatusAndDesc(-1, "没有该堂考试");
            return result;
        }
        ExamPaper record = new ExamPaper();
        record.setExamId(examId);
        List<ExamPaper> examPaperList = this.examPaperDao.selectByCondition(record, new BaseQuery());
        if (examPaperList == null || examPaperList.size() == 0) {
            result.setStatusAndDesc(-2, "该堂考试没有考生试卷");
            return result;
        }
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "userId", ExamPaper.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);

        Map<Integer, ProblemInfo> problemInfoCache = new HashMap<>();
        Map<Integer, ExamParam> examParamCache = new HashMap<>();
        Map<Integer, ResultInfo> resultInfoCache = new HashMap<>();
        Map<Integer, JudgerInfo> judgerInfoCache = new HashMap<>();
        // 问题缓存
        List<SelectProblemNumRe> selectProblemNumReList = paperProblemDao.selectProblemNum(examId);
        List<ProblemInfo> problemInfoListTemp = selectRecordByIds(
                getFieldByList(selectProblemNumReList, "probId", SelectProblemNumRe.class),
                "probId", (BaseDao) problemInfoDao, ProblemInfo.class);
        for (ProblemInfo item : problemInfoListTemp) {
            problemInfoCache.put(item.getProbId(), item);
        }
        // 试卷参数缓存
        ExamParam examParamTemp = new ExamParam();
        examParamTemp.setExamId(examId);
        List<ExamParam> examParamListTemp = examParamDao.selectByCondition(examParamTemp, new BaseQuery());
        for (ExamParam item : examParamListTemp) {
            examParamCache.put(item.getExaParId(), item);
        }
        // judger缓存
        List<JudgerInfo> JudgerInfoListTemp = SiteCache.judgerInfoList;
        for (JudgerInfo item : JudgerInfoListTemp) {
            judgerInfoCache.put(item.getJudId(), item);
        }
        // resultInfo缓存
        List<ResultInfo> resultInfoListTemp = SiteCache.resultInfoList;
        for (ResultInfo item : resultInfoListTemp) {
            resultInfoCache.put(item.getResuId(), item);
        }

        List<Map<String, Object>> dataList = new ArrayList<>();
        UserLoginLog userLoginLogTemp = new UserLoginLog();
        for (int i = 0; i < examPaperList.size(); i++) {
            ExamPaper examPaper = examPaperList.get(i);
            Map<String, Object> dataMap = new HashMap<>();// 存放该试卷的基本信息
            // 获取用户考号
            UserInfo userInfo = userInfoList.get(i);
            dataMap.put("Number", userInfo != null ? userInfo.getUsername() : null);
            // 这段用于获取该试卷考生真实姓名和学号
            UserProfile userProfile = userProfileList.get(i);
            dataMap.put("username", userProfile != null ? userProfile.getRealName() : null);
            dataMap.put("studentNum", userProfile != null ? userProfile.getStudentNumber() : null);

            // 查询当前试卷下的所有题目
            PaperProblem paperProblem = new PaperProblem();
            paperProblem.setExamPaperId(examPaper.getExaPapId());

            List<PaperProblem> paperProblemList = this.paperProblemDao.selectByCondition(paperProblem, new BaseQuery());
            List<ProblemInfo> problemInfoList = new ArrayList<>();
            List<ExamParam> examParamList = new ArrayList<>();
            // 获取提交信息，以最后一次AC或者最好一次提交为准
            List<SubmitInfo> submitInfoList = selectRecordByIds(
                    getFieldByList(paperProblemList, "submitId", PaperProblem.class),
                    "submId", (BaseDao) submitInfoDao, SubmitInfo.class);
            for (int j = 0; j < paperProblemList.size(); j++) {
                PaperProblem item = paperProblemList.get(j);
                // 获取对应的题目，做个简单的缓存
                ProblemInfo problemInfo = problemInfoCache.get(item.getProblemId());
                if (problemInfo == null) problemInfo = new ProblemInfo();
                problemInfoList.add(problemInfo);
                // 获取对应的试卷参数，做个简单的缓存
                ExamParam examParam = examParamCache.get(item.getExamParamId());
                if (examParam == null) examParam = new ExamParam();
                examParamList.add(examParam);
            }
            dataMap.put("paperProblemList", paperProblemList);
            dataMap.put("submitInfoList", submitInfoList);
            dataMap.put("problemInfoList", problemInfoList);
            dataMap.put("examParamList", examParamList);
            dataMap.put("examPaper", examPaper);
            dataList.add(dataMap);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

        Integer length = dataList.size();
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE) + File.separator + new Date().getTime() + "/";
        // 在服务器端创建文件夹
        String examTitle = getFileName(examInfo.getTitle());
        File file = new File(path + examTitle);
        if (!file.exists()) {
            boolean temp = file.mkdirs();
        }
        List<File> srcfile = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Map<String, Object> tempMap = dataList.get(i);
            String username = (String) tempMap.get("username");
            String Number = (String) tempMap.get("Number");
            ExamPaper examPaper = (ExamPaper) tempMap.get("examPaper");
            String exprId = examPaper.getExaPapId().toString();
            String studentNumber = (String) tempMap.get("studentNum");
            String studentName = "\\" + (Number + "_" + username + "_" + studentNumber + "_" + exprId);
            List<File> fileList = new ArrayList<>();
            try {
                File studentFile = new File(path + examTitle + studentName);
                if (!studentFile.exists()) {
                    studentFile.mkdir();
                }
                List<PaperProblem> paperProblemList = (List<PaperProblem>) tempMap.get("paperProblemList");
                List<SubmitInfo> submitInfoList = (List<SubmitInfo>) tempMap.get("submitInfoList");
                List<ProblemInfo> problemInfoList = (List<ProblemInfo>) tempMap.get("problemInfoList");
                List<ExamParam> examParamList = (List<ExamParam>) tempMap.get("examParamList");

                for (int j = 0; j < paperProblemList.size(); j++) {
                    SubmitInfo submitInfo = submitInfoList.get(j);
                    JudgerInfo judgerInfo = judgerInfoCache.get(submitInfo.getJudgerId());
                    ResultInfo resultInfo = resultInfoCache.get(submitInfo.getStatus());
                    String suffix = judgerInfo == null ? ".txt" : judgerInfo.getSuffix();
                    String probTitle = getFileName(problemInfoList.get(j).getTitle());
                    String filePath = path + examTitle + studentName + "\\" + probTitle
                            + "_" + problemInfoList.get(j).getProbId() + suffix;
                    File tempFile = new File(filePath);
                    FileOutputStream out = new FileOutputStream(filePath);
                    BufferedOutputStream buff = null;
                    StringBuilder write = new StringBuilder();
                    String enter = "\r\n";
                    String codeTemp = submitInfo.getSource() == null ? "该考生没有提交代码" : submitInfo.getSource();
                    String code = CipherUtil.encode(codeTemp);
                    try {
                        buff = new BufferedOutputStream(out);
                        String dateString = "null";
                        if (submitInfo.getCreateTime() != null) {
                            dateString = sdf.format(submitInfo.getCreateTime());
                        }
                        String ACStatu = resultInfo == null ? "null" : resultInfo.getName();
                        write.append("/**" + enter);
                        write.append(" * 题目：" + problemInfoList.get(j).getTitle() + enter);
                        write.append(" * 题目分数：" + examParamList.get(j).getScore() + enter);
                        write.append(" * 考试名称：" + examTitle + enter);
                        write.append(" * 考生IP：" + submitInfo.getIp() + enter);
                        write.append(" * AC状态：" + ACStatu + enter);
                        write.append(" * 得分：" + paperProblemList.get(j).getScore() + enter);
                        write.append(" * 提交时间：" + dateString + enter);
                        write.append(" * 考生考号：" + Number + enter);
                        write.append(" * 考生姓名：" + username + enter);
                        write.append(" * 考生学号：" + studentNumber + enter);
                        write.append(" * 代码Hash值：" + code + enter);
                        write.append(" */" + enter);
                        write.append(codeTemp + enter);
                        write.append(enter);
                        buff.write(write.toString().getBytes("GBK"));
                        buff.flush();
                        buff.close();
                    } catch (Exception e) {
                        log.error("生成文本失败 error: {}", e);
                        result.setStatusAndDesc(-3, "生成文本失败" + e.getMessage());
                        return result;
                    } finally {
                        try {
                            buff.close();
                            out.close();
                        } catch (Exception e) {
                            log.error("生成文本失败 error: {}", e);
                            result.setStatusAndDesc(-4, "生成文本失败" + e.getMessage());
                        }
                    }
                    fileList.add(tempFile);
                }
            } catch (Exception e) {
                log.error("生成文本失败 error: {}", e);
                result.setStatusAndDesc(-3, "生成文本失败" + e.getMessage());
                return result;
            }
            File zipfile = new File(path + examTitle + studentName + ".zip");
            ZipUtil.zipFiles(fileList, zipfile);
            srcfile.add(zipfile);
        }
        File dataZipfile = new File(path + examTitle + ".zip");
        ZipUtil.zipFiles(srcfile, dataZipfile);
        if (result.getStatus() == 0) {
            result.setStatus(1);
            result.setFileDir(dataZipfile.getPath());
        }
        return result;
    }

    @Override
    public ImportDataRe importGradeByExamId(Integer examId) {
        ImportDataRe result = new ImportDataRe();
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examId == null || examInfo == null) {
            result.setStatusAndDesc(-1, "没有该堂考试");
            return result;
        }
        // 获取需要填 入的数据
        ExamPaper record = new ExamPaper();
        record.setExamId(examId);
        BaseQuery sortQuery = new BaseQuery();
        sortQuery.setCustom("sort", "sort");
        List<ExamPaper> examPaperList = this.examPaperDao.selectByCondition(record, sortQuery);
        if (examPaperList == null || examPaperList.size() == 0) {
            result.setStatusAndDesc(-2, "该堂考试没有考生试卷");
            return result;
        }
        List<UserInfo> userInfoList = selectRecordByIds(
                getFieldByList(examPaperList, "userId", ExamPaper.class),
                "userId", (BaseDao) userInfoDao, UserInfo.class);
        List<UserProfile> userProfileList = selectRecordByIds(
                getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                "useProId", (BaseDao) userProfileDao, UserProfile.class);
        ExamParam examParam = new ExamParam();
        examParam.setExamId(examId);
        int paramTotal = examParamDao.selectByConditionGetCount(examParam, new BaseQuery());
        if (paramTotal == 0) {
            result.setStatusAndDesc(-3, "改堂考试没有试卷参数");
            return result;
        }

        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE) + File.separator + new Date().getTime();
        String examTitle = getFileName(examInfo.getTitle());
        String fileName = examTitle + ".xls";
        File downFile = new File(path, fileName);
        WritableWorkbook wk = null;
        try {
            //判断路径是否存在，如果不存在就创建一个
            if (!downFile.getParentFile().exists()) {
                downFile.getParentFile().mkdirs();
            }
            wk = Workbook.createWorkbook(downFile); // 创建可写入的Excel工作薄，且内容将写入到输出流，
        } catch (Exception e) {
            log.error("Workbook.createWorkbook file: {} error: {}", downFile.getPath(), e);
            result.setStatusAndDesc(-4, "createWorkbook失败");
            return result;
        }
        try {
            WritableSheet sheet = wk.createSheet("成绩表", 0); // 创建可写入的Excel工作表
            sheet.mergeCells(0, 0, 6 + paramTotal * 4, 0);// 单元格合并方法
            // 字体对象,黑体,字号12、粗体、非斜体、不带下划线、亮蓝色
            WritableFont titleFont = new WritableFont(
                    WritableFont.createFont("黑体"), 12,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            // 创建WritableCellFormat对象，将该对象应用于单元格从而设置单元格的样式
            WritableCellFormat titleFormat = new WritableCellFormat();
            titleFormat.setFont(titleFont); // 设置字体格式
            titleFormat.setAlignment(Alignment.CENTRE);// 设置文本水平居中对齐
            titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置文本垂直居中对齐
            titleFormat.setBackground(Colour.GRAY_25); // 设置背景颜色
            titleFormat.setWrap(true); // 设置自动换行
            // 添加Label对象，参数依次表示在第一列，第一行，内容，使用的格式
            // 将定义好的Label对象添加到工作表上，这样工作表的第一列第一行的内容为‘学员考试成绩一览表’并应用了titleFormat定义的样式
            sheet.addCell(new Label(0, 0, examInfo.getTitle() + "考试成绩表", titleFormat));
            WritableCellFormat cloumnTitleFormat = new WritableCellFormat();
            cloumnTitleFormat.setFont(new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false));
            cloumnTitleFormat.setAlignment(Alignment.CENTRE);
            sheet.addCell(new Label(0, 1, "考号", cloumnTitleFormat));
            sheet.addCell(new Label(1, 1, "学号", cloumnTitleFormat));
            sheet.addCell(new Label(2, 1, "姓名", cloumnTitleFormat));
            sheet.addCell(new Label(3, 1, "班级", cloumnTitleFormat));
            sheet.addCell(new Label(4, 1, "考试成绩", cloumnTitleFormat));
            sheet.addCell(new Label(5, 1, "AC题数", cloumnTitleFormat));
            sheet.addCell(new Label(6, 1, "考试日期", cloumnTitleFormat));
            for (int i = 0; i < paramTotal; i++) {
                sheet.addCell(new Label(7 + i * 4, 1, "第" + (i + 1) + "题题目", cloumnTitleFormat));
                sheet.addCell(new Label(8 + i * 4, 1, "第" + (i + 1) + "题总分", cloumnTitleFormat));
                sheet.addCell(new Label(9 + i * 4, 1, "第" + (i + 1) + "题得分", cloumnTitleFormat));
                sheet.addCell(new Label(10 + i * 4, 1, "第" + (i + 1) + "题AC状态", cloumnTitleFormat));
            }
            DateFormat df = new DateFormat("yyyy-MM-dd hh:mm:ss"); // 定义日期格式
            WritableCellFormat datewcf = new WritableCellFormat(df); // 创建WritableCellFormat对象
            NumberFormat nf = new NumberFormat("0"); // 定义数字格式
            WritableCellFormat wcf = new WritableCellFormat(nf);
            Map<Integer, ProblemInfo> problemInfoCache = new HashMap<>();
            Map<Integer, ExamParam> examParamCache = new HashMap<>();
            Map<Integer, ResultInfo> resultInfoCache = new HashMap<>();
            List<ExamParam> examParamList = examParamDao.selectByCondition(examParam, new BaseQuery());
            for (ExamParam item : examParamList) {
                examParamCache.put(item.getExaParId(), item);
            }
            List<ResultInfo> resultInfoList = resultInfoDao.selectByCondition(new ResultInfo(), new BaseQuery());
            for (ResultInfo item : resultInfoList) {
                resultInfoCache.put(item.getResuId(), item);
            }
            Integer length = examPaperList.size();
            for (int i = 0; i < length; i++) {
                sheet.addCell(new Label(0, i + 2, userInfoList.get(i).getUsername()));
                sheet.addCell(new Label(1, i + 2, userProfileList.get(i).getStudentNumber()));
                sheet.addCell(new Label(2, i + 2, userProfileList.get(i).getRealName()));
                sheet.addCell(new Label(3, i + 2, userProfileList.get(i).getClassName()));

                sheet.addCell(new jxl.write.Number(4, i + 2, examPaperList.get(i).getScore(), wcf));
                sheet.addCell(new jxl.write.Number(5, i + 2, examPaperList.get(i).getAcedCount(), wcf));
                sheet.addCell(new DateTime(6, i + 2, examInfo.getStartTime(), datewcf));

                PaperProblem paperProblem = new PaperProblem();
                paperProblem.setExamPaperId(examPaperList.get(i).getExaPapId());
                List<PaperProblem> paperProblemList = this.paperProblemDao.selectByCondition(paperProblem, new BaseQuery());
                for (int j = 0; j < paperProblemList.size(); j++) {
                    PaperProblem item = paperProblemList.get(j);
                    Integer probId = item.getProblemId();
                    ProblemInfo problemInfo = problemInfoCache.computeIfAbsent(probId, new Function<Integer, ProblemInfo>() {
                        @Override
                        public ProblemInfo apply(Integer integer) {
                            return problemInfoDao.selectByPrimaryKey(probId);
                        }
                    });
                    sheet.addCell(new Label(7 + j * 4, i + 2, problemInfo.getTitle()));
                    Integer examParamId = item.getExamParamId();
                    ExamParam examParam1 = examParamCache.computeIfAbsent(examParamId, new Function<Integer, ExamParam>() {
                        @Override
                        public ExamParam apply(Integer integer) {
                            return examParamDao.selectByPrimaryKey(item.getExamParamId());
                        }
                    });
                    sheet.addCell(new jxl.write.Number(8 + j * 4, i + 2, examParam1.getScore(), wcf));
                    sheet.addCell(new jxl.write.Number(9 + j * 4, i + 2, item.getScore(), wcf));
                    ResultInfo resultInfo = resultInfoCache.get(item.getIsAced());
                    String status = " ";
                    if (resultInfo != null) {
                        status = resultInfo.getName();
                    }
                    sheet.addCell(new Label(10 + j * 4, i + 2, status));
                }

            }
        } catch (WriteException e) {
            log.error("构建sheet失败 file: {} error: {}", downFile.getPath(), e);
            result.setStatusAndDesc(-5, "构建sheet失败: " + e.getMessage());
        }
        try {
            // 将定义的工作表输出到之前指定的介质中（这里是客户端浏览器）
            wk.write();
        } catch (IOException e) {
            log.error("wk.write失败 file: {} error: {}", downFile.getPath(), e);
            result.setStatusAndDesc(-6, "wk.write失败: " + e.getMessage());
        } finally {
            // 操作完成时，关闭对象，释放占用的内存空间
            try {
                wk.close();
            } catch (Exception e) {
                log.error("wk.close file: {} error: {}", downFile.getPath(), e);
            }
        }
        if (result.getStatus() == 0) {
            result.setStatus(1);
            result.setFileDir(downFile.getPath());
            //result.setFileName(fileName);
        }
        return result;
    }

    @Override
    public APIResult selectById(Integer examId) {
        APIResult apiResult = new APIResult();
        ExamInfo examInfo = examInfoDao.selectByPrimaryKey(examId);
        if (examInfo == null) {
            apiResult.setStatusAndDesc(-1, "查询失败");
        } else {
            apiResult.setStatusAndDesc(1, "查询成功");
            apiResult.setDataKey("examInfo", examInfo);
        }
        return apiResult;
    }

    @Override
    public APIResult updateById(ExamInfo examInfo) {
        APIResult apiResult = new APIResult();
        if (examInfo == null) {
            apiResult.setStatusAndDesc(-2, "examInfo不能为空");
            return apiResult;
        }
        int result = examInfoDao.updateByPrimaryKeySelective(examInfo);
        if (result != 1) {
            apiResult.setStatusAndDesc(-1, "更新失败");
        } else {
            apiResult.setStatusAndDesc(1, "更新成功");
        }
        return apiResult;
    }

    @Override
    public APIResult buildExamState(APIResult apiResult, List<ExamInfo> examInfoList, boolean containUModify) {
        List<Integer> peopleTotal = new ArrayList<>();
        List<Integer> statusList = new ArrayList<>();
        List<Integer> proState = new ArrayList<>();
        ExamPaper examPaper = new ExamPaper();
        Date time = new Date();
        for (int i = 0; i < examInfoList.size(); i++) {
            ExamInfo item = examInfoList.get(i);
            examPaper.setExamId(item.getExamId());
            int temp = examPaperDao.selectByConditionGetCount(examPaper, new BaseQuery());
            peopleTotal.add(temp);
            Integer status;
            if (time.getTime() > item.getEndTime().getTime()) {
                status = 2;
            } else if (time.getTime() > item.getStartTime().getTime()) {
                status = 1;
            } else {
                status = 0;
            }
            statusList.add(status);
            PaperProblem paperProblem = new PaperProblem();
            paperProblem.setExamId(item.getExamId());
            int size = paperProblemDao.selectByConditionGetCount(paperProblem, new BaseQuery());
            proState.add(size == 0 ? 0 : 1);
        }
        if (containUModify) {
            List<UserInfo> userInfoList = selectRecordByIds(
                    getFieldByList(examInfoList, "modifyUserId", ExamInfo.class),
                    "userId", (BaseDao) userInfoDao, UserInfo.class);
            List<UserProfile> userProfileList = selectRecordByIds(
                    getFieldByList(userInfoList, "userProfileId", UserInfo.class),
                    "useProId", (BaseDao) userProfileDao, UserProfile.class);
            apiResult.setDataKey("userInfoList", userInfoList);
            apiResult.setDataKey("userProfileList", userProfileList);
        }
        apiResult.setDataKey("peopleTotal", peopleTotal);
        apiResult.setDataKey("statusList", statusList);
        apiResult.setDataKey("proState", proState);
        return apiResult;
    }
}
