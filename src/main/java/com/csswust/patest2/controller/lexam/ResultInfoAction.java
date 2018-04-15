package com.csswust.patest2.controller.lexam;

import com.csswust.patest2.common.cache.SiteCache;
import com.csswust.patest2.controller.common.BaseAction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/3/18.
 */
@RestController
@RequestMapping("/resultInfo")
public class ResultInfoAction extends BaseAction {
    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition() {
        Map<String, Object> res = new HashMap<>();
        res.put("total", SiteCache.resultInfoTotal);
        res.put("data", SiteCache.resultInfoList);
        return res;
    }
}
