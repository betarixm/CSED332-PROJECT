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
    void testPackageCalculate(){
        CodeLineMetric packageCodeLineMetric = new PackageCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/metrics");
        Assertions.assertEquals(packageCodeLineMetric.calculate(), 140.0);

        CodeLineMetric package2CodeLineMetric = new PackageCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/");
        Assertions.assertEquals(package2CodeLineMetric.calculate(), 357.0);
    }

    @Test
    void testClassCalculate(){
        CodeLineMetric classCodeLineMetric = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/metrics/ClassCodeLineMetric.java");
        Assertions.assertEquals(classCodeLineMetric.calculate(), 29.0);

        CodeLineMetric classCodeLineMetric2 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/metrics/CodeLineMetric.java");
        Assertions.assertEquals(classCodeLineMetric2.calculate(), 32.0);

        CodeLineMetric classCodeLineMetric3 = new ClassCodeLineMetric(projectCodeLineMetric.getPath() + "/org/csed332/project/team2/metrics/CompositeCodeLineMetric.java");
        Assertions.assertEquals(classCodeLineMetric3.calculate(), 45.0);
    }
}
