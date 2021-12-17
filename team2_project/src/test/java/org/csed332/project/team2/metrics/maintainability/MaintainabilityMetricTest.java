package org.csed332.project.team2.metrics.maintainability;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.metrics.codeline.ClassCodeLineMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MaintainabilityMetricTest {
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
    public void testMICalculation() throws Exception {
        configureFixture("MainClass.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            ClassCodeLineMetric codeLineMetric = new ClassCodeLineMetric(psiClass);
                            HalsteadMetric halsteadMetric = new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOLUME);
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);

                            codeLineMetric.calculate();
                            halsteadMetric.calculate();
                            cyclomaticMetric.calculate();

                            MaintainabilityMetric maintainabilityMetric = new MaintainabilityMetric(codeLineMetric, halsteadMetric, cyclomaticMetric);
                            maintainabilityMetric.calculate();

                            Assertions.assertEquals(90.3357, maintainabilityMetric.get(), 0.00005);
                        });
    }
}
