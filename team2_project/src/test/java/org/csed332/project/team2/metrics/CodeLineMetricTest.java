package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.junit.jupiter.api.*;

import java.util.List;

public class CodeLineMetricTest {
    private static final String testPath = "TestProjects/SingleFiles";
    private static FixtureHelper helperMainClass;

    @BeforeAll
    public static void initialize() throws Exception {
        String fileName = "MainClass.java";
        helperMainClass = FixtureHelper.getInstance();
        helperMainClass.changeFile(testPath);
        helperMainClass.setUp();
        helperMainClass.configure(fileName);
    }

    @AfterAll
    public static void dispose() throws Exception {
        helperMainClass.tearDown();
    }

    @Test
    public void testProjectCodeLineMetricCalculateNoPackages() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            CodeLineMetric projectCodeLineMetric = new ProjectCodeLineMetric(project);
                            Assertions.assertEquals(0.0, projectCodeLineMetric.calculate());
                        });
    }

    @Test
    public void testClassCodeLineMetricCalculate() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();
                            CodeLineMetric classCodeLineMetric = new ClassCodeLineMetric(psiClass);
                            Assertions.assertEquals(1.0, classCodeLineMetric.calculate());
                        });
    }
}
