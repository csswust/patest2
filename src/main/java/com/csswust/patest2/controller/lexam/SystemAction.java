package com.csswust.patest2.controller.lexam;

import com.baidu.ueditor.ActionEnter;
import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.listener.ApplicationStartListener;
import com.csswust.patest2.listener.OnlineListener;
import com.csswust.patest2.service.OnlineUserService;
import com.csswust.patest2.service.judge.JudgeThread;
import com.csswust.patest2.service.monitor.MonitorRe;
import com.csswust.patest2.service.monitor.MonitorService;
import com.csswust.patest2.service.result.OnlineListRe;
import com.csswust.patest2.utils.SystemInfo;
import com.csswust.patest2.utils.SystemUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/system")
public class SystemAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(SystemAction.class);

    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private MonitorService monitorService;

    private static final String[] monitorKeys = new String[]{
            "userInfoSelect"
    };

    @ResponseBody
    @RequestMapping(value = "/monitor", method = {RequestMethod.GET, RequestMethod.POST})
    public Object monitor(
            @RequestParam Integer number,
            @RequestParam Long timeUnit) {
        APIResult apiResult = new APIResult();
        for (int i = 0; i < monitorKeys.length; i++) {
            List<MonitorRe> list = monitorService.getDataByKey(monitorKeys[i], number, timeUnit);
            apiResult.setDataKey(monitorKeys[i], list);
        }
        return apiResult;
    }

    @ResponseBody
    @RequestMapping(value = "/authError", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> authError() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "权限不足");
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/refreshConfig", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> refreshConfig() throws Exception {
        Map<String, Object> map = new HashMap<>();
        Config.refreshConfig();
        map.put("status", 1);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/selectOnline", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectOnline(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        OnlineListRe result = onlineUserService.getOnlineList(userName,
                studentNumber, page, rows);
        res.put("onlineListRe", result);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/signOut", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> signOut(@RequestParam String sessinoId) {
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isBlank(sessinoId)) {
            return res;
        }
        HttpSession session = OnlineListener.onlineMap.get(sessinoId);
        if (session == null) {
            return res;
        }
        session.invalidate();
        res.put("status", 1);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/selectSystemInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectSystemInfo() {
        Map<String, Object> res = new HashMap<>();
        List<SystemInfo> list = new ArrayList<>();
        try {
            list.addAll(SystemUtil.property());
            list.addAll(SystemUtil.servlet(request));
            List<JudgeThread> judgeThreadlist = ApplicationStartListener.judgeThreadList;
            StringBuilder judgeStatus = new StringBuilder();
            Date now = new Date();
            for (JudgeThread item : judgeThreadlist) {
                Integer submId = item.getCurrSubmId();
                if (submId != null) {
                    long time = now.getTime() - item.getStartDate().getTime();
                    String temp = format("%d: %d, ", submId, time);
                    judgeStatus.append(temp);
                }
            }
            list.add(new SystemInfo("judgeStatus", "判题状态", judgeStatus));
        } catch (Exception e) {
            log.error("selectSystemInfo error: {}", e);
        }
        res.put("infoList", list);
        return res;
    }


    @ResponseBody
    @RequestMapping(value = "/ueditor", method = {RequestMethod.GET, RequestMethod.POST})
    public String ueditor() {
        String rootPath = Config.get(SiteKey.UPLOAD_UEDITOR_DIR, SiteKey.UPLOAD_UEDITOR_DIR_DE);
        String action = request.getParameter("action");
        // conf.json文件必须放在rootPath/conf/目录下
        String result = new ActionEnter(request, rootPath).exec();
        if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
            rootPath = rootPath.replace("\\", "/");
            result = result.replaceAll(rootPath, "/");
            result = result.replaceAll("D:/", "");
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("cache-control", "no-cache");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (IOException e) {
            log.error("out.writer data:{} error: {}", result, e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> download(
            @RequestParam(required = true, defaultValue = "false") boolean isTempPath,
            @RequestParam(required = true, defaultValue = "false") boolean isUeditorPath,
            @RequestParam(required = false) String path) {
        //下载文件路径
        File file = null;
        if (isTempPath) {
            // 临时路径
            String tempPath = Config.get(SiteKey.UPLOAD_TEMP_DIR, SiteKey.UPLOAD_TEMP_DIR_DE);
            file = new File(tempPath + path);
        } else if (isUeditorPath) {
            // ueditor路径
            String ueditorPath = Config.get(SiteKey.UPLOAD_UEDITOR_DIR, SiteKey.UPLOAD_UEDITOR_DIR_DE);
            int index = path.indexOf("?");
            if (index != -1) path = path.substring(0, index);
            file = new File(ueditorPath + path);
        } else {
            // 非标准路径
            file = new File(path);
        }
        if (!file.isFile() || !file.exists()) return null;
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = null;
        try {
            downloadFielName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("field.get data:{} error: {}", file.getName(), e);
        }
        //通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("data:{} error: {}", file.getName(), e);
        }
        return null;
    }
}
