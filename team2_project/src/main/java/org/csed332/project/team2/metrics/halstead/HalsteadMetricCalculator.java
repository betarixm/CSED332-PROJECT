package org.csed332.project.team2.metrics.halstead;

/**
 * Calculator for Halstead metric
 */
public class HalsteadMetricCalculator {
    private int numberUniqueOperators = 0;
    private int numberUniqueOperands = 0;
    private int numberTotalOperators = 0;
    private int numberTotalOperands = 0;

    /**
     * Instantiates a new HalsteadMetricCalculator.
     *
     * @param N1 the n 1
     * @param n1 the n 1
     * @param N2 the n 2
     * @param n2 the n 2
     */
    public HalsteadMetricCalculator(int N1, int n1, int N2, int n2) {
        numberTotalOperators = N1;
        numberUniqueOperators = n1;
        numberTotalOperands = N2;
        numberUniqueOperands = n2;
    }

    /**
     * Gets vocabulary.
     *
     * @return the vocabulary
     */
    public int getVocabulary() {
        return numberUniqueOperators + numberUniqueOperands;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return numberTotalOperators + numberTotalOperands;
    }

    /**
     * Gets volume.
     *
     * @return the volume
     */
    public double getVolume() {
        return getSize() * (Math.log(getVocabulary()) / Math.log(2));
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public double getDifficulty() {
        return ((double) numberUniqueOperators / 2) * ((double) numberTotalOperators / numberUniqueOperands);
    }

    /**
     * Gets efforts.
     *
     * @return the efforts
     */
    public double getEfforts() {
        double volume = getVolume();
        double difficulty = getDifficulty();

        return volume * difficulty;
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public double getErrors() {
        double volume = getVolume();
        return volume / 3000.0;
    }

    /**
     * Gets testing time.
     *
     * @return the testing time
     */
    public double getTestingTime() {
        double effort = getEfforts();
        // 18 seconds
        return effort / 18;
    }
}
