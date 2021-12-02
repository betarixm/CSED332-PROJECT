package org.csed332.project.team2.metrics;

import com.intellij.debugger.memory.agent.parsers.StringArrayParser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.impl.stores.IProjectStore;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PsiMethodPattern;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclomaticMetricTest {
    private static final String testPath = "CycloTestProject";
    private static FixtureHelper helperMainClass;

    @BeforeAll
    public static void initialize() throws Exception {

        helperMainClass = new FixtureHelper(testPath);
        helperMainClass.setUp();

    }

    @Test
    public void testEmptyCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("EmptyClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("EmptyClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            Assertions.assertEquals(0.0, cyclomaticMetric.calculate());
                        });
    }

    @Test
    public void testAssertCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("AssertTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("AssertTestClass");

                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleAssert")));
                        });
    }

    @Test
    public void testConditionExprCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("ConditionExprTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("ConditionExprTestClass");

                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("declareExpr")));
                            Assertions.assertEquals(1.0, metrics.get(methods.get("returnExpr")));
                            Assertions.assertEquals(1.0, metrics.get(methods.get("blockExpr")));
                            Assertions.assertEquals(2.0, metrics.get(methods.get("nestedExpr")));
                        });
    }

    @Test
    public void testForCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("ForTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("ForTestClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleFor")));
                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleForEach")));
                            Assertions.assertEquals(2.0, metrics.get(methods.get("nestedFor")));
                        });
    }

    @Test
    public void testIFCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("IFTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("IFTestClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleIF")));
                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleIfElse")));
                            Assertions.assertEquals(3.0, metrics.get(methods.get("nestedIF")));
                            Assertions.assertEquals(3.0, metrics.get(methods.get("multiElseIf")));
                        });
    }

    @Test
    public void testSwitchCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("SwitchTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("SwitchTestClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(0.0, metrics.get(methods.get("singleSwitch")));
                            Assertions.assertEquals(0.0, metrics.get(methods.get("defaultSwitch")));
                            Assertions.assertEquals(2.0, metrics.get(methods.get("multiCaseSwitch")));
                            Assertions.assertEquals(2.0, metrics.get(methods.get("nestedSwitch")));
                        });
    }

    @Test
    public void testTryCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("TryTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("TryTestClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleTry")));
                            Assertions.assertEquals(2.0, metrics.get(methods.get("nestedTry")));
                        });
    }

    @Test
    public void testWhileCycloMatic() {
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            try {
                                helperMainClass.configure("WhileTestClass.java");
                            } catch (Exception e) {

                            }

                            final PsiClass psiClass = helperMainClass.getPsiClass("WhileTestClass");
                            CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(psiClass);
                            cyclomaticMetric.calculate();

                            Map<PsiMethod, Double> metrics = cyclomaticMetric.getMetrics().get(psiClass);
                            Map<String, PsiMethod> methods = getMethods(psiClass);

                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleWhile")));
                            Assertions.assertEquals(1.0, metrics.get(methods.get("singleDoWhile")));
                            Assertions.assertEquals(4.0, metrics.get(methods.get("nestedWhile")));
                        });
    }


    public Map<String, PsiMethod> getMethods(PsiClass aClass) {
        Map<String, PsiMethod> result = new HashMap<>();

        for (PsiMethod method : aClass.getMethods()) {
            result.put(method.getName(), method);
        }

        return result;
    }


}
