package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;

/**
 * Created by 972536780 on 2018/3/19.
 */
public class ImportProblmDataRe extends APIResult {
    private String fileDir;
    private String fileName;

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
