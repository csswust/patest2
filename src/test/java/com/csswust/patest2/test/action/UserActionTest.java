package com.csswust.patest2.test.action;

import com.csswust.patest2.test.JunitBaseActionTest;
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
        MockHttpServletResponse response = request("/user/insert", res);
        System.out.println(response.getContentAsString());
    }
}
