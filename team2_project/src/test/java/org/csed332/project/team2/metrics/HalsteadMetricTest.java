package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
                            Assertions.assertEquals(9.0, new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOCABULARY).calculate(), 0.00005);
                            Assertions.assertEquals(50.7188, new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOLUME).calculate(), 0.00005);
                            Assertions.assertEquals(0.5, new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.DIFFICULTY).calculate(), 0.00005);
                            Assertions.assertEquals(25.3594, new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.EFFORT).calculate(), 0.00005);
                        });
    }

    @Test
    public void testSave() throws Exception {
        configureFixture("SimpleInt.java");
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final Project project = helperMainClass.getFixture().getProject();
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            HalsteadMetric halsteadMetric = new HalsteadMetric(psiClass, HalsteadMetric.HalsteadType.VOCABULARY);
                            halsteadMetric.calculate();
                            String testId = UUID.randomUUID().toString();
                            CalcHistoryModel calc = MetricService.generateCalcHistoryModel(testId);
                            halsteadMetric.save(calc);

                            Optional<Map<String, Map<String, Map<String, Double>>>> metricOptional = MetricService.getMetric(calc.getMetric());
                            Assertions.assertNotNull(metricOptional);
                            Assertions.assertTrue(metricOptional.isPresent());
                            Assertions.assertEquals(9.0, metricOptional.get().get(psiClass.getName()).get(psiMethod.getName()).get("VOCABULARY"));

                            cleanDB(calc);
                        });

    }

    void cleanDB(CalcHistoryModel calc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(calc);
            session.getTransaction().commit();
        }
    }
}
