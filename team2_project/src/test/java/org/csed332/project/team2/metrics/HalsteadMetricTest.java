package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HalsteadMetricTest {
    private static final String testPath = "TestProjects/SingleFiles";
    private static FixtureHelper helperMainClass;

    @BeforeAll
    public static void initialize() throws Exception {
        helperMainClass = FixtureHelper.getInstance();
        helperMainClass.changeFile(testPath);
        helperMainClass.setUp();
    }

    @AfterAll
    public static void dispose() throws Exception {
        helperMainClass.tearDown();
    }

    private void configureFixture(String fileName) throws Exception {
        helperMainClass.configure(fileName);
    }

    @Test
    public void parseMethodOneOperator() throws Exception {
        configureFixture("SimpleInt.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();
                            Assertions.assertEquals(9.0,  new HalsteadMetric(psiClass, HalsteadMetric.Type.VOCABULARY).calculate(), 0.00005);
                            Assertions.assertEquals(50.7188,  new HalsteadMetric(psiClass, HalsteadMetric.Type.VOLUME).calculate(), 0.00005);
                            Assertions.assertEquals(0.5,  new HalsteadMetric(psiClass, HalsteadMetric.Type.DIFFICULTY).calculate(), 0.00005);
                            Assertions.assertEquals(25.3594,  new HalsteadMetric(psiClass, HalsteadMetric.Type.EFFORT).calculate(), 0.00005);
                        });
    }
}
