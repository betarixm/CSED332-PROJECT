package org.csed332.project.team2.metrics.maintainability;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
    public void testMIForClass() throws Exception {
        configureFixture("MainClass.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            HalsteadMetric halsteadMetric = new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOLUME);
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            halsteadMetric.calculate();
                            cyclomaticMetric.calculate();

                            MaintainabilityMetric maintainabilityMetric = new MaintainabilityMetric(psiClass, halsteadMetric, cyclomaticMetric);
                            maintainabilityMetric.calculate();

                            Map<PsiMethod, Double> metrics = maintainabilityMetric.getMetrics().get(psiClass.getName());
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(154.4741, metrics.get(methods.get("main")), 0.00005);
                        });
    }

    @Test
    public void testMIForMethods() throws Exception {
        configureFixture("MultiMethods.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            HalsteadMetric halsteadMetric = new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOLUME);
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            halsteadMetric.calculate();
                            cyclomaticMetric.calculate();

                            MaintainabilityMetric maintainabilityMetric = new MaintainabilityMetric(psiClass, halsteadMetric, cyclomaticMetric);
                            maintainabilityMetric.calculate();

                            Map<PsiMethod, Double> metrics = maintainabilityMetric.getMetrics().get(psiClass.getName());
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(154.4741, metrics.get(methods.get("main")), 0.00005);
                            Assertions.assertEquals(151.9495, metrics.get(methods.get("simpleAddition")), 0.00005);
                            Assertions.assertEquals(154.8192, metrics.get(methods.get("simpleInt")), 0.00005);
                        });
    }

    @Test
    public void testMILaterCalculation() throws Exception {
        configureFixture("MainClass.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();

                            HalsteadMetric halsteadMetric = new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOLUME);
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            MaintainabilityMetric maintainabilityMetric = new MaintainabilityMetric(psiClass, halsteadMetric, cyclomaticMetric);

                            halsteadMetric.calculate();
                            cyclomaticMetric.calculate();
                            maintainabilityMetric.calculate();

                            Map<PsiMethod, Double> metrics = maintainabilityMetric.getMetrics().get(psiClass.getName());
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(154.4741, metrics.get(methods.get("main")), 0.00005);
                        });
    }

    void cleanDB(CalcHistoryModel calc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(calc);
            session.getTransaction().commit();
        }
    }

    public Map<String, PsiMethod> getMethods(PsiClass aClass) {
        Map<String, PsiMethod> result = new HashMap<>();

        for (PsiMethod method : aClass.getMethods()) {
            result.put(method.getName(), method);
        }

        return result;
    }

}
