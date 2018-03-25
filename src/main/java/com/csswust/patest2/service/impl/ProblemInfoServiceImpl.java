package com.csswust.patest2.service.impl;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.dao.ProblemInfoDao;
import com.csswust.patest2.entity.ProblemInfo;
import com.csswust.patest2.service.ProblemInfoService;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.service.result.ImportDataRe;
import com.csswust.patest2.service.result.SelectProblemDataRe;
import com.csswust.patest2.utils.FileUtil;
import com.csswust.patest2.utils.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 972536780 on 2018/3/19.
 */
@Service
public class ProblemInfoServiceImpl extends BaseService implements ProblemInfoService {
    private static Logger log = LoggerFactory.getLogger(ProblemInfoServiceImpl.class);

    @Autowired
    private ProblemInfoDao problemInfoDao;

    @Override
    public APIResult insertProblemData(Integer probId, List<String> input, List<String> output) {
        APIResult result = new APIResult();
        if (input.size() != output.size()) {
            result.setStatus(-1);
            result.setDesc("输入数据和输出数据组数不一致");
            return result;
        }
        String datadir = Config.get(SiteKey.JUDGE_STANDARD_DATA_PATH) + File.separator + probId;
        File file = new File(datadir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    boolean temp = files[i].delete();
                    if (!temp) {
                        log.error("files delete file: {}", files[i].getAbsoluteFile());
                        result.setStatus(-2);
                        result.setDesc("清理文件失败" + files[i].getAbsoluteFile());
                        return result;
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            try {
                FileUtil.generateFile(input.get(i), datadir, i + ".in");
                FileUtil.generateFile(output.get(i), datadir, i + ".out");
                count++;
            } catch (Exception e) {
                log.error("FileUtil.generateFile file: {}", datadir + " " + i);
                result.setStatus(-3);
                result.setDesc("输出到文件失败");
                return result;
            }
        }
        result.setStatus(count);
        return result;
    }

    @Override
    public APIResult insertProblemData(Integer probId, MultipartFile zipFile) {
        APIResult result = new APIResult();
        if (zipFile.isEmpty()) {
            result.setStatus(-1);
            result.setDesc("上传文件为空");
            return result;
        }
        String path = Config.get(SiteKey.UPLOAD_TEMP_DIR);
        String filename = zipFile.getOriginalFilename() + (new Date().getTime());
        File filepath = new File(path, filename);
        //判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        File tempFile = new File(path + File.separator + filename);
        try {
            zipFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("zipFile.transferTo file: {} error: {}", tempFile.getAbsoluteFile(), e);
            result.setStatus(-2);
            result.setDesc("复制文件失败");
            return result;
        }
        String datadir = Config.get(SiteKey.JUDGE_STANDARD_DATA_PATH) + File.separator + probId;
        File file = new File(datadir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    boolean temp = files[i].delete();
                    if (!temp) {
                        log.error("files[i].delete file: {}", files[i].getAbsoluteFile());
                        result.setStatus(-3);
                        result.setDesc("清理文件失败" + files[i].getAbsoluteFile());
                        return result;
                    }
                }
            }
        }
        try {
            ZipUtil.unzip(tempFile.getPath(), datadir, false);
        } catch (Exception e) {
            log.error("ZipUtil.unzip file: {} error: {}", tempFile.getAbsoluteFile(), e);
            result.setStatus(-4);
            result.setDesc("解压出错" + e.getMessage());
            return result;
        }
        result.setStatus(1);
        return result;
    }

    @Override
    public SelectProblemDataRe selectProblemData(Integer probId) {
        SelectProblemDataRe result = new SelectProblemDataRe();
        List<String> input = new ArrayList<String>();
        List<String> output = new ArrayList<String>();
        ProblemInfo problemInfo = problemInfoDao.selectByPrimaryKey(probId);
        String datadir = Config.get(SiteKey.JUDGE_STANDARD_DATA_PATH) + File.separator + probId;
        int count = 0;
        if (problemInfo != null) {
            count = problemInfo.getTestdataNum();
            for (int i = 0; i < count; i++) {
                String inputString = null;
                String outputString = null;
                try {
                    inputString = FileUtil.readFile(datadir, i + ".in");
                    outputString = FileUtil.readFile(datadir, i + ".out");
                } catch (Exception e) {
                    log.error("FileUtil.readFile file: {}", datadir + " " + i);
                }
                input.add(inputString);
                output.add(outputString);
            }
        }
        result.setInput(input);
        result.setOutput(output);
        result.setTotal(count);
        result.setStatus(1);
        return result;
    }

    @Override
    public ImportDataRe importProblmData(Integer probId) {
        ImportDataRe result = new ImportDataRe();
        if (probId == null) {
            result.setStatus(-1);
            result.setDesc("问题id不能为空");
            return result;
        }
        String datadir = Config.get(SiteKey.JUDGE_STANDARD_DATA_PATH);
        File tempFile = new File(datadir + File.separator + probId);
        if (!tempFile.exists()) {
            result.setStatus(-2);
            result.setDesc("数据目录不存在");
            return result;
        }
        tempFile = new File(datadir + File.separator + probId + ".zip");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        try {
            ZipUtil.ZipMultiFile(datadir + File.separator + probId, datadir, probId + ".zip");
        } catch (Exception e) {
            log.error("ZipUtil.ZipMultiFile file: {}", datadir);
            result.setStatus(-3);
            result.setDesc("压缩文件失败");
            return result;
        }
        if (tempFile.exists()) {
            result.setFileDir(datadir + "/" + probId + ".zip");
            // result.setFileName();
            result.setStatus(1);
        } else {
            result.setStatus(-4);
        }
        return result;
    }
}
