package org.csed332.project.team2.metrics;

import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class HalsteadMetricTest {

    HalsteadMetric halsteadMetric = new HalsteadMetric(3, 5, 10, 20);

    @Test
    public void calculateVocabulary() {
        Assertions.assertEquals(8, halsteadMetric.getVocabulary());
    }

    @Test
    public void calculateSize() {
        Assertions.assertEquals(30, halsteadMetric.getSize());
    }

    @Test
    public void calculateVolume() {
        Assertions.assertEquals(90.0, halsteadMetric.getVolume());
    }

    @Test
    public void calculateDifficulty() {
        Assertions.assertEquals(3.0, halsteadMetric.getDifficulty());
    }

    @Test
    public void calculateEfforts() {
        Assertions.assertEquals(270.0, halsteadMetric.getEfforts());
    }

    @Test
    public void calculateErrors() {
        Assertions.assertEquals(0.03, halsteadMetric.getErrors());
    }

    @Test
    public void calculateTestingTime() {
        Assertions.assertEquals(15.0, halsteadMetric.getTestingTime());
    }
}
