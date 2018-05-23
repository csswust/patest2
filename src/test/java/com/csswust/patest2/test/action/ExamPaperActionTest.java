package com.csswust.patest2.test.action;

import com.csswust.patest2.test.base.JunitBaseActionTest;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.Map;

public class ExamPaperActionTest extends JunitBaseActionTest {
    public Map<String, String> res = new HashMap<String, String>();

    @Test
    public void drawProblem() throws Exception {
        res.put("examId", "1");
        MockHttpServletResponse response = request("/examPaper/drawProblem", res);
        System.out.println(response.getContentAsString());
    }
}
