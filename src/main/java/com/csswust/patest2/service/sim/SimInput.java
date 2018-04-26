package com.csswust.patest2.service.sim;

import com.csswust.patest2.entity.SubmitInfo;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/25.
 */
public class SimInput {
    private String scriptPath;
    private List<SubmitInfo> leftCodeList;
    private List<SubmitInfo> rightCodeList;
    private String leftCmd;
    private String rightCmd;

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public List<SubmitInfo> getLeftCodeList() {
        return leftCodeList;
    }

    public void setLeftCodeList(List<SubmitInfo> leftCodeList) {
        this.leftCodeList = leftCodeList;
    }

    public List<SubmitInfo> getRightCodeList() {
        return rightCodeList;
    }

    public void setRightCodeList(List<SubmitInfo> rightCodeList) {
        this.rightCodeList = rightCodeList;
    }

    public String getLeftCmd() {
        return leftCmd;
    }

    public void setLeftCmd(String leftCmd) {
        this.leftCmd = leftCmd;
    }

    public String getRightCmd() {
        return rightCmd;
    }

    public void setRightCmd(String rightCmd) {
        this.rightCmd = rightCmd;
    }
}
