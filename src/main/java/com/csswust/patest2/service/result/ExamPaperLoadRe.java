package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;

/**
 * Created by 972536780 on 2018/3/21.
 */
public class ExamPaperLoadRe extends APIResult {
    private String userNameError;
    private String dirPath;
    private String fileName;

    public String getUserNameError() {
        return userNameError;
    }

    public void setUserNameError(String userNameError) {
        this.userNameError = userNameError;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
