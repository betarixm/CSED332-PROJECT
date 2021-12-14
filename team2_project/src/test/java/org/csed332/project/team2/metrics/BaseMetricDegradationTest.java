package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BaseMetricDegradationTest {
    private static BaseMetric baseMetric;
    private static final String testPath = "CycloTestProject";
    private static FixtureHelper helperMainClass;
    private static FixtureHelper anotherClass;
    private static WarningCondition cond;
    private String testId;
    private CalcHistoryModel calc;

    @BeforeAll
    public static void initialize() throws Exception {
        helperMainClass = FixtureHelper.getInstance();
        helperMainClass.changeFile(testPath);
        helperMainClass.setUp();

        cond = new WarningCondition(WarningCondition.Mode.INCREASE);
    }

    @BeforeEach
    public void initBaseMetric(){
        baseMetric = new BaseMetric() {
            @Override
            public double calculate() {
                return 0;
            }

            @Override
            public void save(CalcHistoryModel calc)
            {
                Map<PsiClass, Map<PsiMethod, Double>> metrics = getMetrics();
                for (PsiClass _class : metrics.keySet())
                {
                    for (PsiMethod _method : metrics.get(_class).keySet())
                    {
                        Double _figure = metrics.get(_class).get(_method);
                        MetricService.addMetric(getID(), _class.getName(), _method.getName(), "", _figure, calc);
                    }
                }
            }
        };
        testId = UUID.randomUUID().toString();
        calc = MetricService.generateCalcHistoryModel(testId);
        baseMetric.setCondition(cond);
    }

    @Test
    public void testGetDegradationMetrics(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            baseMetric.setMetric(1.5, psiClass, psiMethod);
                            baseMetric.save(calc);

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            Map<PsiClass, Set<PsiMethod>> degradationMetrics = baseMetric.getDegradationMetrics();

                            Assertions.assertEquals(degradationMetrics.keySet(), Set.of(psiClass));
                            Assertions.assertEquals(degradationMetrics.get(psiClass), Set.of(psiMethod));
                        });
    }

    @Test
    public void testGetDegradationMetricsMultiple(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass1 = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod11 = psiClass1.getMethods()[0];

                            try {
                                helperMainClass.configure("ForTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass2 = helperMainClass.getPsiClass("ForTestClass");
                            final PsiMethod psiMethod21 = psiClass2.getMethods()[0];
                            final PsiMethod psiMethod22 = psiClass2.getMethods()[1];

                            baseMetric.setMetric(1.5, psiClass1, psiMethod11);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod21);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod22);
                            baseMetric.save(calc);

                            // Phase 2
                            baseMetric.setMetric(2.0, psiClass1, psiMethod11);
                            baseMetric.setMetric(2.0, psiClass2, psiMethod21);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod22);
                            baseMetric.save(calc);

                            Map<PsiClass, Set<PsiMethod>> degradationMetrics = baseMetric.getDegradationMetrics();

                            Assertions.assertEquals(degradationMetrics.keySet(), Set.of(psiClass1, psiClass2));
                            Assertions.assertEquals(degradationMetrics.get(psiClass1), Set.of(psiMethod11));
                            Assertions.assertEquals(degradationMetrics.get(psiClass2), Set.of(psiMethod21));
                        });
    }

    @Test
    public void testGetDegradationMetricsNoWarningOneClass(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass1 = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod11 = psiClass1.getMethods()[0];

                            try {
                                helperMainClass.configure("ForTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass2 = helperMainClass.getPsiClass("ForTestClass");
                            final PsiMethod psiMethod21 = psiClass2.getMethods()[0];
                            final PsiMethod psiMethod22 = psiClass2.getMethods()[1];

                            // Phase 1
                            baseMetric.setMetric(2.0, psiClass1, psiMethod11);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod21);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod22);
                            baseMetric.save(calc);

                            // Phase 2
                            baseMetric.setMetric(2.0, psiClass1, psiMethod11);
                            baseMetric.setMetric(2.0, psiClass2, psiMethod21);
                            baseMetric.setMetric(1.0, psiClass2, psiMethod22);
                            baseMetric.save(calc);

                            Map<PsiClass, Set<PsiMethod>> degradationMetrics = baseMetric.getDegradationMetrics();

                            Assertions.assertEquals(degradationMetrics.keySet(), Set.of(psiClass2));
                            Assertions.assertEquals(degradationMetrics.get(psiClass2), Set.of(psiMethod21));
                        });
    }

    @Test
    public void testGetDegradationMetricsNoDegradation(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            Map<PsiClass, Set<PsiMethod>> degradationMetrics = baseMetric.getDegradationMetrics();

                            Assertions.assertTrue(degradationMetrics.keySet().isEmpty());
                        });
    }

    @Test
    public void testCheckDegradationTrue(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            baseMetric.setMetric(1.5, psiClass, psiMethod);
                            baseMetric.save(calc);

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            Assertions.assertTrue(baseMetric.checkDegradation());
                        });
    }

    @Test
    public void testCheckDegradationFalse(){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("AssertTestClass");
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            baseMetric.setMetric(2.0, psiClass, psiMethod);
                            baseMetric.save(calc);

                            Assertions.assertFalse(baseMetric.checkDegradation());
                        });
    }

    @AfterAll
    public static void dispose() throws Exception {
        helperMainClass.tearDown();
    }

    @AfterEach
    void cleanDB()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(calc);
            session.getTransaction().commit();
        }
    }
}
