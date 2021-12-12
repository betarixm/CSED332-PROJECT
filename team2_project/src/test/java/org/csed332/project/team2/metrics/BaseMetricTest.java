package org.csed332.project.team2.metrics;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.FixtureHelper;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BaseMetricTest {
    PsiClass class1, class2;
    private static final String testPath = "TestProjects/SingleFiles";
    private static FixtureHelper helperMainClass;
    private static BaseMetric baseMetric;
    private static WarningCondition moreCond, increaseCond;

    @BeforeAll
    public static void initialize() throws Exception {
        String fileName = "MainClass.java";
        helperMainClass = FixtureHelper.getInstance();
        helperMainClass.changeFile(testPath);
        helperMainClass.setUp();
        helperMainClass.configure(fileName);

        baseMetric = new BaseMetric() {
            @Override
            public double calculate() {
                return 0;
            }
        };

        moreCond = new WarningCondition(WarningCondition.Mode.MORE_THAN, 5);
        increaseCond = new WarningCondition(WarningCondition.Mode.INCREASE);
    }

    @AfterAll
    public static void dispose() throws Exception {
        helperMainClass.tearDown();
    }

    private List<Double> saveAndGetFromDB(List<Double> list, PsiClass psiClass, PsiMethod psiMethod){
        for(Double value: list){
            baseMetric.setMetric(value, psiClass, psiMethod);
            baseMetric.save();
        }
        String savedName = psiClass.getName() + "@" + psiMethod.getName();
        List<MetricModel> metrics = MetricModelService.getMetrics(baseMetric.getID(), savedName, list.size());

        return metrics.stream().map(MetricModel::getFigure).collect(Collectors.toList());
    }

    public void testSave(List<Double> valueList){
        ApplicationManager.getApplication()
                .invokeAndWait(
                        () -> {
                            final PsiClass psiClass = helperMainClass.getFirstPsiClass();
                            final PsiMethod psiMethod = psiClass.getMethods()[0];

                            List<Double> reversedList = new ArrayList<>(valueList);
                            Collections.reverse(reversedList);
                            Assertions.assertEquals(saveAndGetFromDB(valueList, psiClass, psiMethod), reversedList);
                        });
    }

    @Test
    public void testSaveEmpty(){
        testSave(List.of());
    }

    @Test
    public void testSaveOne(){
        testSave(List.of(2.0));
    }

    @Test
    public void testSaveMultiple(){
        testSave(List.of(2.0, 3.0, 1.0));
    }


    public void testWarn(List<Double> vauleList, Boolean warning){
        testSave(vauleList);
        Assertions.assertEquals(baseMetric.checkDegradation(), warning);
    }

    /*@Test
    public void testWarnEmpty(){
        testWarn(List.of(), false);
    }

    @Test
    public void testWarnMoreCondOne(){
        baseMetric.setWarningCondition(moreCond);
        testWarn(List.of(1.0), false);
        testWarn(List.of(6.0), true);
        testWarn(List.of(5.0), true);
    }

    @Test
    public void testWarnIncreaseCond(){
        baseMetric.setWarningCondition(increaseCond);
        testWarn(List.of(1.0, 0.0), false);
        testWarn(List.of(1.0, 1.0), false);
        testWarn(List.of(0.0, 1.0), true);
    }*/
}