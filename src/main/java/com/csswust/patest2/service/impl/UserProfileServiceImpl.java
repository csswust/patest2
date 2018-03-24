package com.csswust.patest2.service.impl;

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
import com.csswust.patest2.service.result.UserProfileLoadRe;
import jxl.Sheet;
import jxl.Workbook;
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

    /**
     * 事务，失败回滚
     *
     * @param multipartFile excel
     * @param isIgnoreError 是否忽略错误
     * @return UserProfileLoadRe
     */
    @Transactional
    @Override
    public UserProfileLoadRe insertByExcel(MultipartFile multipartFile, boolean isIgnoreError) {
        UserProfileLoadRe loadRe = new UserProfileLoadRe();
        if (multipartFile.isEmpty()) {
            loadRe.setStatus(-1);
            loadRe.setDesc("上传文件为空");
            return loadRe;
        }
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR);
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
            log.error("multipartFile.transferTo file: {} error: {}",
                    excelFile.getAbsoluteFile(), e);
            loadRe.setStatus(-2);
            loadRe.setDesc("复制文件失败");
            return loadRe;
        }
        InputStream in;
        try {
            in = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            log.error("new FileInputStream file: {} error: {}",
                    excelFile.getAbsoluteFile(), e);
            loadRe.setStatus(-3);
            loadRe.setDesc("创建文件流失败");
            return loadRe;
        }
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(in);
        } catch (Exception e) {
            log.error("Workbook.getWorkbook file: {} error: {}",
                    excelFile.getAbsoluteFile(), e);
            loadRe.setStatus(-4);
            loadRe.setDesc("解析excel失败");
            return loadRe;
        }
        List<UserProfile> errorUserProfileList = new ArrayList<>();
        int count = 0;
        here:
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheet(i);
            int rowNum = sheet.getRows();
            if (rowNum >= 2000) {
                loadRe.setStatus(-6);
                loadRe.setDesc("单次导入不能超过2000条");
                return loadRe;
            }
            for (int j = 1; j < rowNum; j++) {
                UserProfile userProfile = new UserProfile();
                userProfile.setStudentNumber(sheet.getCell(0, j).getContents());
                userProfile.setRealName(sheet.getCell(1, j).getContents());
                userProfile.setClassName(sheet.getCell(2, j).getContents());
                userProfile.setEntranceYear(StringToInt(sheet.getCell(5, j).getContents()));
                String majorName = sheet.getCell(3, j).getContents();
                String academyName = sheet.getCell(4, j).getContents();
                AcademyInfo academyInfoTemp = academyInfoDao.selectByAcademyName(academyName);
                MajorInfo majorInfo = new MajorInfo();
                AcademyInfo academyInfo = new AcademyInfo();
                if (academyInfoTemp == null) {
                    academyInfo.setAcademyName(academyName);
                    academyInfoDao.insertSelective(academyInfo);
                    majorInfo.setMajorName(majorName);
                    majorInfo.setAcademyId(academyInfo.getAcaId());
                    majorInfoDao.insertSelective(majorInfo);
                    userProfile.setMajorId(majorInfo.getMajId());
                } else {
                    MajorInfo majorInfoCondition = new MajorInfo();
                    majorInfoCondition.setMajorName(majorName);
                    majorInfoCondition.setAcademyId(academyInfoTemp.getAcaId());
                    List<MajorInfo> majorInfoList = majorInfoDao.selectByCondition(
                            majorInfoCondition, new BaseQuery(1, 1));
                    if (majorInfoList == null || majorInfoList.size() == 0) {
                        majorInfo.setMajorName(majorName);
                        majorInfo.setAcademyId(academyInfoTemp.getAcaId());
                        majorInfoDao.insertSelective(majorInfo);
                        userProfile.setMajorId(majorInfo.getMajId());
                    } else {
                        userProfile.setMajorId(majorInfoList.get(0).getMajId());
                    }
                }
                userProfile.setIsStudent(1);
                int status = userProfileDao.insertSelective(userProfile);
                if (status == 1) {
                    count++;
                } else {
                    if (isIgnoreError) {
                        errorUserProfileList.add(userProfile);
                    } else {
                        // 回滚事务
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        loadRe.setStatus(-5);
                        loadRe.setError(userProfile);
                        loadRe.setDesc(format("由于%s插入失败，回滚所有数据", userProfile.getStudentNumber()));
                        break here;
                    }
                }
            }
        }
        try {
            workbook.close();
            in.close();
        } catch (IOException e) {
            log.error("workbook.close file: {} error: {}", excelFile.getAbsoluteFile(), e);
        }
        if (loadRe.getStatus() != 0) {
            return loadRe;
        }
        loadRe.setErrorList(errorUserProfileList);
        loadRe.setStatus(count);
        return loadRe;
    }
}
