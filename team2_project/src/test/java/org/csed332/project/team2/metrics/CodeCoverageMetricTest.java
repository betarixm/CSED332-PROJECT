package org.csed332.project.team2.metrics;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.csed332.project.team2.metrics.coverage.ProjectCodeCoverageMetric;
import org.junit.Test;

public class CodeCoverageMetricTest extends BasePlatformTestCase {
    @Test
    public void testCodeCoverageCalculate(){
        ProjectCodeCoverageMetric projectCodeCoverageMetric = new ProjectCodeCoverageMetric(getProject());

        assertEquals(projectCodeCoverageMetric.calculateForClass(CodeCoverageMetricTest.class), 0.25);
        // 0.25 is dummy value
    }
}