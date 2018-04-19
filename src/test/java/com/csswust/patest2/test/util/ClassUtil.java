package com.csswust.patest2.test.util;

import com.csswust.patest2.controller.ep.*;
import com.csswust.patest2.controller.lexam.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 972536780 on 2018/3/24.
 */
public class ClassUtil {
    public static void main(String[] args) {

        List<Class> classList = Arrays.asList(
                AcademyInfoAction.class, CourseInfoAction.class,
                ExamInfoAction.class, ExamNoticeAction.class,
                ExamPaperAction.class, ExamParamAction.class,
                ExamProblemAction.class, JudgerInfoAction.class,
                KnowledgeInfoAction.class, MajorInfoAction.class,
                PaperProblemAction.class, ProblemInfoAction.class,
                ResultInfoAction.class, SiteInfoAction.class,
                StudentAction.class, SubmitInfoAction.class,
                SubmitResultAction.class, SubmitSimilarityAction.class,
                SystemAction.class, UserInfoAction.class,
                UserProfileAction.class,

                EplApplyInfoAction.class,EplNoticeAction.class, EplOrderInfoAction.class,
                EpAction.class,EpApplyInfoAction.class,
                EpExamInfoAction.class, EpExamPaperAction.class,
                EpExamParamAction.class, EpExamProblemAction.class,
                EpNoticeAction.class, EpOrderInfoAction.class,
                EpUserInfoAction.class
        );

        for (Class item : classList) {
            RequestMapping lei = ((RequestMapping) item.getAnnotation(RequestMapping.class));
            if (lei == null) continue;
            String path1 = lei.value()[0];
            Method[] methods = item.getMethods();
            // System.out.print("\""+path1 + "/*\""+",");
            for (Method method : methods) {
                RequestMapping fanfa = ((RequestMapping) method.getAnnotation(RequestMapping.class));
                if (fanfa == null) continue;
                String path2 = fanfa.value()[0];
                System.out.println(path1 + path2);
            }
        }
    }
}
