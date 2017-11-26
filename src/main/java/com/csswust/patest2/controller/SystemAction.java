package com.csswust.patest2.controller;

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
}
