package org.csed332.project.team2.metrics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CodeLineMetricTest {
    CodeLineMetric projectCodeLineMetric;

    @BeforeEach
    void initialize(){
        projectCodeLineMetric = new ProjectCodeLineMetric();
    }

    @Test
    void testProjectCalculate(){
        Assertions.assertEquals(projectCodeLineMetric.calculate(), 357.0);
    }

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
    void testClassCalculate(){
        CodeLineMetric classCodeLineMetric = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/metrics/ClassCodeLineMetric.java");
        Assertions.assertEquals(classCodeLineMetric.calculate(), 29.0);

        CodeLineMetric classCodeLineMetric2 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1/HighInterestAccount.java");
        Assertions.assertEquals(classCodeLineMetric2.calculate(), 48.0);

        CodeLineMetric classCodeLineMetric3 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/edu/postech/csed332/homework1/LowInterestAccount.java");
        Assertions.assertEquals(classCodeLineMetric3.calculate(), 48.0);
    }
}
