package com.csswust.patest2.controller;

import com.baidu.ueditor.ActionEnter;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.service.result.ImportProblmDataRe;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemAction extends BaseAction {
    @RequestMapping(value = "/authError", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> authError() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "权限不足");
        return map;
    }

    @RequestMapping(value = "/ueditor", method = {RequestMethod.GET, RequestMethod.POST})
    public String ueditor() throws Exception {
        String rootPath = request.getServletContext().getRealPath("/");
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("patest2")) + "ueditor/";
        String action = request.getParameter("action");
        String result = new ActionEnter(request, rootPath).exec();
        if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
            rootPath = rootPath.replace("\\", "/");
            result = result.replaceAll(rootPath, "/");
        }
        return result;
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> download(
            @RequestParam(required = true) String path,
            @RequestParam(required = true) String fileName) {
        //下载文件路径
        File file = new File(path + File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = null;
        try {
            downloadFielName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
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
