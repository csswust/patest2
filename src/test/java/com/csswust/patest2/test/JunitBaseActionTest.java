package com.csswust.patest2.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:application.xml")
public class JunitBaseActionTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        System.out.println("========== Action Test Start ============");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("========== Action Test End   ============");
    }

    private MockHttpServletRequestBuilder requestBuilder(String url,
                                                         Map<String, String> res) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url).characterEncoding("UTF-8");
        for (String key : res.keySet()) {
            requestBuilder.param(key, res.get(key));
        }
        return requestBuilder;
    }

    public MockHttpServletResponse request(String url,
                                           Map<String, String> res) {
        MvcResult result;
        try {
            result = mockMvc.perform(requestBuilder(url, res)).andReturn();
            return result.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
