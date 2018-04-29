package com.csswust.patest2.service.sim;

import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.entity.SubmitInfo;
import com.csswust.patest2.service.common.BaseService;
import com.csswust.patest2.utils.FileUtil;
import com.csswust.patest2.utils.StreamUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 972536780 on 2018/4/25.
 */
@Service
public class SimServiceImpl extends BaseService implements SimService {
    private static Logger log = LoggerFactory.getLogger(SimServiceImpl.class);

    @Override
    public SimOutput judge(SimInput simInput) {
        SimOutput simOutput = new SimOutput();
        if (simInput.getScriptPath() == null) {
            simOutput.setError("脚本目录不存在");
            return simOutput;
        }
        String scriptPath = simInput.getScriptPath();
        // 获取工作目录，这里面包括源码，编译后文件，允许结果文件等
        String workPath = getPath(SiteKey.SIM_WORK_DIR, SiteKey.SIM_WORK_DIR_DE);// 包括时间目录
        String ownedPath = format("%d_%d", new Date().getTime(), simInput.hashCode());
        String finalWorkPath = workPath + "/" + ownedPath;
        boolean writeStatus = writeCodeToFile(finalWorkPath, simInput);
        if (!writeStatus) {
            log.error("sim 写入文件失败 data: {}", getJson(simInput));
            simOutput.setError("sim 写入文件失败");
            return simOutput;
        }
        try {
            File file = new File(finalWorkPath, "/result.txt");
            if (!file.exists()) file.createNewFile();
            // 构建命令行命令
            StringBuilder cmd = new StringBuilder();
            cmd.append(scriptPath + " ")
                    .append("-epS -o ")
                    .append(finalWorkPath + "/result.txt ")
                    .append(simInput.getLeftCmd())
                    .append("| ")
                    .append(simInput.getRightCmd());
            FileUtil.generateFile(cmd.toString(), finalWorkPath,
                    "cmd.txt");
            // 执行
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd.toString());
            // 设置超时时间
            int timeOut = Config.getToInt(SiteKey.SIM_MAX_RUN_TIME, SiteKey.SIM_MAX_RUN_TIME_DE);
            // 这里有个坑，当cpu负载太高的时候，可能很多代码没办法正确判断
            boolean status = proc.waitFor(timeOut, TimeUnit.SECONDS);
            if (!status) {
                proc.destroy();
                simOutput.setError("执行超时");
                return simOutput;
            }
            // 获得错误信息，由于缓冲区原因，当error信息非常大的时候，会导致线程阻塞
            String errMsg = StreamUtil.output(proc.getErrorStream());
            if (StringUtils.isNotBlank(errMsg)) {
                simOutput.setError(errMsg);
                return simOutput;
            }
            String result = FileUtil.readFile(finalWorkPath, "result.txt");
            log.info("sim info data :{}", result);
            analysisResult(finalWorkPath, result, simOutput);
        } catch (Exception e) {
            log.error("sim error data :{} error: {}", getJson(simInput), e);
            simOutput.setError(e.getMessage());
        } finally {
            // 删除源文件，如果没有执行将会导致判题系统堵塞
            int isDeleteDir = Config.getToInt(SiteKey.SIM_IS_DELETE_DIR, SiteKey.SIM_IS_DELETE_DIR_DE);
            if (isDeleteDir == 1) {
                FileUtils.deleteQuietly(new File(finalWorkPath));
            }
        }
        return simOutput;
    }

    private boolean analysisResult(String workPath, String result, SimOutput simOutput) {
        if (StringUtils.isBlank(result)) {
            simOutput.setError("结果为空");
            return false;
        }
        List<SimResult> simResultList = new ArrayList<>();
        // 按指定模式在字符串查找
        String pattern = workPath + "/file1/(\\d+).txt consists for (\\d+) % of "
                + workPath + "/file2/(\\d+).txt material";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(result);
        while (m.find()) {
            try {
                SimResult simResult = new SimResult();
                Integer submId1 = Integer.parseInt(m.group(1));
                Integer submId2 = Integer.parseInt(m.group(3));
                Integer value = Integer.parseInt(m.group(2));
                simResult.setSubmId1(submId1);
                simResult.setSubmId2(submId2);
                simResult.setValue(value * 0.01);
                simResultList.add(simResult);
            } catch (Exception e) {
                log.error("Matcher.find data :{} error: {}", getJson(result), e);
            }
        }
        simOutput.setSimResultList(simResultList);
        return simResultList.size() != 0;
    }

    private boolean writeCodeToFile(String workPath, SimInput simInput) {
        List<SubmitInfo> left = simInput.getLeftCodeList();
        List<SubmitInfo> right = simInput.getRightCodeList();
        if (left == null || left.size() == 0) return false;
        if (right == null || left.size() == 0) return false;
        int temp1 = writeByList(workPath + "/file1", left);
        int temp2 = writeByList(workPath + "/file2", right);
        simInput.setLeftCmd(getCmd(workPath + "/file1", left));
        simInput.setRightCmd(getCmd(workPath + "/file2", right));
        return temp1 != 0 && temp2 != 0;
    }

    private int writeByList(String workPath, List<SubmitInfo> list) {
        int count = 0;
        if (list == null || list.size() == 0) return count;
        for (int i = 0; i < list.size(); i++) {
            SubmitInfo item = list.get(i);
            if (item == null) continue;
            if (item.getSubmId() == null) continue;
            if (item.getSource() == null) continue;
            FileUtil.generateFile(item.getSource(), workPath,
                    item.getSubmId() + ".txt");
            count = count + 1;
        }
        return count;
    }

    private String getCmd(String workPath, List<SubmitInfo> list) {
        StringBuilder builder = new StringBuilder();
        if (list == null || list.size() == 0) return builder.toString();
        for (int i = 0; i < list.size(); i++) {
            SubmitInfo item = list.get(i);
            if (item == null) continue;
            if (item.getSubmId() == null) continue;
            builder.append(workPath + "/" + item.getSubmId() + ".txt ");
        }
        return builder.toString();
    }
}
