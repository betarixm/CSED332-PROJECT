package org.csed332.project.team2.metrics;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CodeLineMetricTest {
    CodeLineMetric projectCodeLineMetric;
    // set with your absolute path of your project
    // test project path: D:/subin/lectures_local/csed332/submit_hw/homework1/problem1
    String projectPath = "D:/subin/lectures_local/csed332/submit_hw/homework1/problem1";

    @BeforeEach
    public void initialize() {
        // set with your absolute path of your project
        projectCodeLineMetric = new ProjectCodeLineMetric(this.projectPath);
    }


    @Test
    public void testProjectCalculate() {
        Assertions.assertEquals(projectCodeLineMetric.calculate(), 271.0);
    }

    // test package path: D:/subin/lectures_local/csed332/submit_hw/homework1/problem1/src/main/java/edu/postech/csed332/homework1
    @Test
    public void testPackageCalculate() {
        // set with your absolute path of your package
        CodeLineMetric packageCodeLineMetric = new PackageCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1");
        Assertions.assertEquals(packageCodeLineMetric.calculate(), 271.0);
    }

    /*
    test class list:
    ACCTYPE.java  Bank.java                 IllegalOperationException.java
    Account.java  HighInterestAccount.java  LowInterestAccount.java
     */
    @Test
    public void testClassCalculate() {
        CodeLineMetric classCodeLineMetric = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1/Bank.java");
        Assertions.assertEquals(classCodeLineMetric.calculate(), 109.0);

        CodeLineMetric classCodeLineMetric2 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1/HighInterestAccount.java");
        Assertions.assertEquals(classCodeLineMetric2.calculate(), 48.0);

        CodeLineMetric classCodeLineMetric3 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1/LowInterestAccount.java");
        Assertions.assertEquals(classCodeLineMetric3.calculate(), 48.0);
    }

    @Test
    public void testDBConnection() {
        CodeLineMetric classCodeLineMetric = new ClassCodeLineMetric(projectPath + "/src/main/java" + "/edu/postech/csed332/homework1/Bank.java");
        List<MetricModel> metricModelList = MetricModelService.getMetrics(classCodeLineMetric.getID(), "Bank");
        for (MetricModel metricModel : metricModelList) MetricModelService.remove(metricModel);

        Assertions.assertEquals(classCodeLineMetric.getID(), "code-line");

        // get before calculate
        Assertions.assertEquals(classCodeLineMetric.get(), -1);
        Assertions.assertEquals(classCodeLineMetric.calculate(), 109.0);

        // get after calculate
        Assertions.assertEquals(classCodeLineMetric.get(), 109.0);

        // get from DB
        CodeLineMetric classCodeLineMetric2 = new ClassCodeLineMetric(projectPath + "/src/main/java" + "/edu/postech/csed332/homework1/Bank.java");
        Assertions.assertEquals(classCodeLineMetric2.get(), 109.0);
    }
}
