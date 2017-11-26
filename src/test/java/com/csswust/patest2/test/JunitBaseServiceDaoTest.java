package com.csswust.patest2.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:application.xml"})
public class JunitBaseServiceDaoTest {
    @Before
    public void setUp() throws Exception {
        System.out.println("========== Service Or Dao Test Start ============");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("========== Service Or Dao Test End   ============");
    }
}
