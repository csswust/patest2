package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.dao.MajorInfoDao;
import com.csswust.patest2.dao.UserProfileDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.AcademyInfo;
import com.csswust.patest2.entity.MajorInfo;
import com.csswust.patest2.entity.UserProfile;
import com.csswust.patest2.service.UserProfileService;
import com.csswust.patest2.service.common.BaseService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 972536780 on 2018/3/16.
 */
@Service
public class UserProfileServiceImpl extends BaseService implements UserProfileService {
    private static Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private MajorInfoDao majorInfoDao;
    @Autowired
    private AcademyInfoDao academyInfoDao;

    private final class ExcelInfo {
        private String studentNumber;
        private String realName;
        private String className;
        private String majorName;
        private String academyName;
        private Integer entranceYear;

        private Boolean isError = true;
        private Integer majorId;
        private Integer academyId;

        public Boolean getError() {
            return isError;
        }

        public void setError(Boolean error) {
            isError = error;
        }

        public Integer getMajorId() {
            return majorId;
        }

        public void setMajorId(Integer majorId) {
            this.majorId = majorId;
        }

        public Integer getAcademyId() {
            return academyId;
        }

        public void setAcademyId(Integer academyId) {
            this.academyId = academyId;
        }

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMajorName() {
            return majorName;
        }

        public void setMajorName(String majorName) {
            this.majorName = majorName;
        }

        public String getAcademyName() {
            return academyName;
        }

        public void setAcademyName(String academyName) {
            this.academyName = academyName;
        }

        public Integer getEntranceYear() {
            return entranceYear;
        }

        public void setEntranceYear(Integer entranceYear) {
            this.entranceYear = entranceYear;
        }
    }

    private APIResult checkExcel(MultipartFile multipartFile, boolean isIgnoreError) {
        APIResult apiResult = new APIResult();
        if (multipartFile.isEmpty()) {
            apiResult.setStatusAndDesc(-1, "上传文件为空");
            return apiResult;
        }
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE);
        String filename = multipartFile.getOriginalFilename() + (new Date().getTime());
        File filepath = new File(path, filename);
        //判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        File excelFile = new File(path + File.separator + filename);
        try {
            multipartFile.transferTo(excelFile);
        } catch (IOException e) {
            log.error("multipartFile.transferTo file: {} error: {}", excelFile.getAbsoluteFile(), e);
            apiResult.setStatusAndDesc(-2, "复制文件失败");
            return apiResult;
        }
        InputStream in;
        try {
            in = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            log.error("new FileInputStream file: {} error: {}", excelFile.getAbsoluteFile(), e);
            apiResult.setStatusAndDesc(-3, "创建文件流失败");
            return apiResult;
        }
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(in);
        } catch (Exception e) {
            log.error("Workbook.getWorkbook file: {} error: {}", excelFile.getAbsoluteFile(), e);
            apiResult.setStatusAndDesc(-4, "解析excel失败");
            return apiResult;
        }
        List<ExcelInfo> excelInfoList = new ArrayList<>();
        List<String> numberExistErrorList = new ArrayList<>();
        List<String> majorExistErrorList = new ArrayList<>();
        List<String> academyExistErrorList = new ArrayList<>();
        here:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            int rowNum = sheet.getRows();
            if (rowNum >= 2000) {
                apiResult.setStatusAndDesc(-6, "单次导入不能超过2000条");
                break;
            }
            for (int j = 1; j < rowNum; j++) {
                ExcelInfo excelInfo = new ExcelInfo();
                String studentNumber = sheet.getCell(0, j).getContents();
                excelInfo.setStudentNumber(studentNumber);
                excelInfo.setRealName(sheet.getCell(1, j).getContents());
                excelInfo.setClassName(sheet.getCell(2, j).getContents());
                excelInfo.setEntranceYear(StringToInt(sheet.getCell(5, j).getContents()));
                String majorName = sheet.getCell(3, j).getContents();
                String academyName = sheet.getCell(4, j).getContents();
                excelInfo.setMajorName(majorName);
                excelInfo.setAcademyName(academyName);
                excelInfoList.add(excelInfo);
                // 首先校验学号
                if (StringUtils.isBlank(studentNumber)) {
                    apiResult.setStatusAndDesc(-7, "学号不能为空");
                    break here;
                }
                UserProfile condition = new UserProfile();
                condition.setStudentNumber(studentNumber);
                int isExistNumber = userProfileDao.selectByConditionGetCount(condition, new BaseQuery(1, 1));
                if (isExistNumber != 0) {
                    if (!numberExistErrorList.contains(studentNumber)) {
                        numberExistErrorList.add(studentNumber);
                    }
                    continue;// 如果存在，那么就不在校验
                }
                excelInfo.setError(false);// 表示此学生还未导入到数据库中
                // 校验学院
                if (StringUtils.isBlank(academyName)) {
                    apiResult.setStatusAndDesc(-7, "学院不能为空");
                    break here;
                }
                AcademyInfo academyInfoTemp = academyInfoDao.selectByAcademyName(academyName);
                if (academyInfoTemp == null) {
                    if (!academyExistErrorList.contains(academyName)) {
                        academyExistErrorList.add(academyName);
                    }
                    continue;
                }
                excelInfo.setAcademyId(academyInfoTemp.getAcaId());
                // 校验专业
                if (StringUtils.isBlank(majorName)) {
                    apiResult.setStatusAndDesc(-7, "专业不能为空");
                    break here;
                }
                MajorInfo majorInfoCondition = new MajorInfo();
                majorInfoCondition.setMajorName(majorName);
                majorInfoCondition.setAcademyId(academyInfoTemp.getAcaId());
                List<MajorInfo> majorInfoList = majorInfoDao.selectByCondition(majorInfoCondition, new BaseQuery(1, 1));
                if (majorInfoList == null || majorInfoList.size() == 0) {
                    if (!majorExistErrorList.contains(majorName)) {
                        majorExistErrorList.add(majorName);
                    }
                    continue;
                }
                MajorInfo majorInfoTemp = majorInfoList.get(0);
                excelInfo.setMajorId(majorInfoTemp.getMajId());
            }
        }
        try {
            workbook.close();
            in.close();
        } catch (IOException e) {
            log.error("workbook.close file: {} error: {}", excelFile.getAbsoluteFile(), e);
        }
        if (apiResult.getStatus() == 0) {
            apiResult.setDataKey("excelInfoList", excelInfoList);
            apiResult.setDataKey("numberExistErrorList", numberExistErrorList);
            apiResult.setDataKey("majorExistErrorList", majorExistErrorList);
            apiResult.setDataKey("academyExistErrorList", academyExistErrorList);
            apiResult.setStatusAndDesc(1, "解析成功");
        }
        return apiResult;
    }


    /**
     * 事务，失败回滚
     *
     * @param multipartFile excel
     * @param isIgnoreError 是否忽略错误
     * @return UserProfileLoadRe
     */
    @Transactional
    @Override
    public APIResult insertByExcel(MultipartFile multipartFile, boolean isIgnoreError) {
        APIResult apiResult = new APIResult();
        APIResult checkResult = checkExcel(multipartFile, isIgnoreError);
        if (checkResult.getStatus() != 1) {
            apiResult.setStatusAndDesc(checkResult.getStatus(), checkResult.getDesc());
            return apiResult;
        }
        List<ExcelInfo> excelInfoList = (List<ExcelInfo>) checkResult.getData().get("excelInfoList");
        List<String> numberExistErrorList = (List<String>) checkResult.getData().get("numberExistErrorList");
        List<String> majorExistErrorList = (List<String>) checkResult.getData().get("majorExistErrorList");
        List<String> academyExistErrorList = (List<String>) checkResult.getData().get("academyExistErrorList");
        // 如果有学院或者专业不存在，报错
        boolean flag = false;
        if (academyExistErrorList.size() != 0) {
            flag = true;
            apiResult.setDataKey("academyExistErrorList", academyExistErrorList);
        }
        if (majorExistErrorList.size() != 0) {
            flag = true;
            apiResult.setDataKey("majorExistErrorList", majorExistErrorList);
        }
        if (!isIgnoreError && numberExistErrorList.size() != 0) {
            flag = true;
            apiResult.setDataKey("numberExistErrorList", numberExistErrorList);
        }
        if (flag) {
            apiResult.setStatusAndDesc(-200, "学号、学院、或者专业错误");
            return apiResult;
        }
        int count = 0;
        for (int i = 0; i < excelInfoList.size(); i++) {
            ExcelInfo excelInfo = excelInfoList.get(i);
            if (isIgnoreError && excelInfo.getError()) continue;
            UserProfile userProfile = new UserProfile();
            userProfile.setStudentNumber(excelInfo.getStudentNumber());
            userProfile.setRealName(excelInfo.getRealName());
            userProfile.setClassName(excelInfo.getClassName());
            userProfile.setEntranceYear(excelInfo.getEntranceYear());
            userProfile.setMajorId(excelInfo.getMajorId());
            userProfile.setIsStudent(1);
            int status = userProfileDao.insertSelective(userProfile);
            if (status == 1) {
                count++;
            } else {
                // 回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                apiResult.setStatusAndDesc(-10, format("由于%s插入失败，回滚所有数据（可能原因是：名单的学号存在冲突）", userProfile.getStudentNumber()));
                break;
            }
        }
        if (apiResult.getStatus() == 0) {
            apiResult.setDataKey("count", count);
            apiResult.setDataKey("error", excelInfoList.size() - count);
            apiResult.setStatusAndDesc(1, "成功");
        }
        return apiResult;
    }
}
