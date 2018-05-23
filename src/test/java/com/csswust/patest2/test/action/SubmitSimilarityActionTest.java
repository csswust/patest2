package com.csswust.patest2.test.action;

import com.csswust.patest2.test.base.JunitBaseActionTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 972536780 on 2018/5/11.
 */
public class SubmitSimilarityActionTest extends JunitBaseActionTest {
    public Map<String, String> res = new HashMap<String, String>();

    @Test
    public void getSimByExamId() throws Exception {
        res.put("examId", "14");
        MockHttpServletResponse response = request("/submitSimilarity/getSimByExamId", res);
        System.out.println(response.getContentAsString());
    }
}
