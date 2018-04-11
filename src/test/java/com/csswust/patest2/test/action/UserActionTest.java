package com.csswust.patest2.test.action;

import com.csswust.patest2.test.base.JunitBaseActionTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class UserActionTest extends JunitBaseActionTest {
    public Map<String, String> res = new HashMap<String, String>();

    @Test
    public void insert() throws Exception {
        res.put("username", "zhangsan");
        res.put("password", "1234");
        res.put("studentNumber", "5120141014");
        res.put("teacher", "1");
        res.put("admin", "1");
        MockHttpServletResponse response = request("/user/insertUserInfo", res);
        System.out.println(response.getContentAsString());
    }
}
