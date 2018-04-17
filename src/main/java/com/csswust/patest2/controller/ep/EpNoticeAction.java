package com.csswust.patest2.controller.ep;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.controller.common.BaseAction;
import com.csswust.patest2.dao.EpNoticeDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.entity.EpNotice;
import com.csswust.patest2.service.EpNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/4/17.
 */
@RestController
@RequestMapping("/ep")
public class EpNoticeAction extends BaseAction {
    @Autowired
    private EpNoticeService epNoticeService;

    @RequestMapping(value = "/epNotice/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectByCondition(
            EpNotice epNotice,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        return epNoticeService.selectByCondition(epNotice,page,rows);
    }
}
