package org.csed332.project.team2.metrics;

import org.csed332.project.team2.metrics.halstead.HalsteadMetricCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HalsteadMetricCalculatorTest {
    HalsteadMetricCalculator halsteadMetricCalculator = new HalsteadMetricCalculator(10, 3, 20, 5);

    @Test
    public void calculateVocabulary() {
        Assertions.assertEquals(8, halsteadMetricCalculator.getVocabulary());
    }

    @Test
    public void calculateSize() {
        Assertions.assertEquals(30, halsteadMetricCalculator.getSize());
    }

    @Test
    public void calculateVolume() {
        Assertions.assertEquals(90.0, halsteadMetricCalculator.getVolume());
    }

    @Test
    public void calculateDifficulty() {
        Assertions.assertEquals(3.0, halsteadMetricCalculator.getDifficulty());
    }

    @Test
    public void calculateEfforts() {
        Assertions.assertEquals(270.0, halsteadMetricCalculator.getEfforts());
    }

    @Test
    public void calculateErrors() {
        Assertions.assertEquals(0.03, halsteadMetricCalculator.getErrors());
    }

    @Test
    public void calculateTestingTime() {
        Assertions.assertEquals(15.0, halsteadMetricCalculator.getTestingTime());
    }
}
