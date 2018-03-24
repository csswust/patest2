package com.csswust.patest2.controller;

import com.baidu.ueditor.ActionEnter;
import com.csswust.patest2.common.config.Config;
import com.csswust.patest2.common.config.SiteKey;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.service.OnlineUserService;
import com.csswust.patest2.service.result.OnlineListRe;
import com.csswust.patest2.utils.SystemInfo;
import com.csswust.patest2.utils.SystemUtil;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemAction extends BaseAction {
    private static Logger log = LoggerFactory.getLogger(SystemAction.class);

    @Autowired
    private OnlineUserService onlineUserService;

    @ResponseBody
    @RequestMapping(value = "/authError", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> authError() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "权限不足");
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/selectOnline", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectOnline(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        Map<String, Object> res = new HashMap<>();
        OnlineListRe result = onlineUserService.getOnlineList(page, rows);
        res.put("onlineListRe", result);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/selectSystemInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectSystemInfo() {
        Map<String, Object> res = new HashMap<>();
        List<SystemInfo> list = new ArrayList<SystemInfo>();
        try {
            list.addAll(SystemUtil.property());
            list.addAll(SystemUtil.servlet(request));
        } catch (Exception e) {
            log.error("selectSystemInfo error: {}", e);
        }
        res.put("infoList", list);
        return res;
    }


    @RequestMapping(value = "/ueditor", method = {RequestMethod.GET, RequestMethod.POST})
    public String ueditor() throws Exception {
        //String rootPath = request.getServletContext().getRealPath("/");
        String rootPath = Config.get(SiteKey.UPLOAD_UEDITOR_DIR);
        String action = request.getParameter("action");
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
            e.printStackTrace();
        } finally {
            out.close();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> download(
            @RequestParam(required = true, defaultValue = "false") boolean isTempPath,
            @RequestParam(required = true, defaultValue = "false") boolean isUeditorPath,
            @RequestParam(required = false) String path,
            @RequestParam(required = false) String fileName) {
        //下载文件路径
        String tempPath = Config.get(SiteKey.UPLOAD_TEMP_DIR);
        File file = null;
        if (isTempPath) {
            file = new File(tempPath + path + File.separator + fileName);
        } else if (isUeditorPath) {
            String ueditorPath = Config.get(SiteKey.UPLOAD_UEDITOR_DIR);
            int index = path.indexOf("?");
            if (index != -1) path = path.substring(0, index);
            file = new File(ueditorPath + path);
        } else {
            file = new File(path + File.separator + fileName);
        }
        if (!file.isFile() || !file.exists()) return null;
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = null;
        try {
            downloadFielName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
