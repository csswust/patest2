package com.csswust.patest2.controller;

import com.baidu.ueditor.ActionEnter;
import com.csswust.patest2.controller.common.BaseAction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
