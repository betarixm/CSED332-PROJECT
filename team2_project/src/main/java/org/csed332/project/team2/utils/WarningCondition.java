package org.csed332.project.team2.utils;

/**
 * Defines condition for metric warning.
 */
public class WarningCondition {

    private double threshold;
    private double teta;
    private Mode mode;

    /**
     * Instantiates a new WarningCondition.
     *
     * @param mode the mode
     */
    public WarningCondition(Mode mode) {
        setMode(mode);
        teta = 0;
        threshold = 0;
    }

    /**
     * Instantiates a new WarningCondition.
     *
     * @param mode      the mode
     * @param threshold the threshold
     */
    public WarningCondition(Mode mode, double threshold) {
        this(mode);
        setThreshold(threshold);
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Sets threshold.
     *
     * @param value the threshold
     */
    public void setThreshold(double value) {
        this.threshold = value;
    }

    /**
     * Sets teta.
     *
     * @param value the teta
     */
    public void setTeta(double value) {
        this.teta = value;
    }

    /**
     * Returns true if the condition for warning is satisfied, otherwise return false.
     *
     * @param oldValue the old value
     * @param newValue the new value
     * @return the boolean
     */
    public Boolean shouldWarn(double oldValue, double newValue) {
        switch (mode) {
            case DECREASE:
                return oldValue - newValue > teta;
            case INCREASE:
                return newValue - oldValue > teta;
            case LESS_THAN:
                return newValue <= threshold;
            case MORE_THAN:
                return newValue >= threshold;
            default:
                return false;
        }
    }

    /**
     * Return number of values needed for each condition mode.
     *
     * @return the int
     */
    public int numberOfValuesNeeded() {
        switch (mode) {
            case DECREASE:
                return 2;
            case INCREASE:
                return 2;
            case LESS_THAN:
                return 1;
            case MORE_THAN:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * The enum Mode.
     */
    public enum Mode {INCREASE, DECREASE, MORE_THAN, LESS_THAN, NO_WARNING}
}
