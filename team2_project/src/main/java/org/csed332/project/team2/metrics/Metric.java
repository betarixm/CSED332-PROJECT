package org.csed332.project.team2.metrics;


/**
 * The interface Metric.
 */
public interface Metric {
    /**
     * Get metric figure.
     *
     * @return the figure
     */
    double get();

    /**
     * Calculate metric.
     *
     * @return the figure
     */
    double calculate();

    /**
     * Gets id.
     *
     * @return the id
     */
    String getID();

    /**
     * Check degradation of metric.
     * Return true if metric has degraded, otherwise false.
     *
     * @return the boolean
     */
    boolean checkDegradation();

    /**
     * Save metric data to database.
     */
    void save();

    /**
     * The enum Type.
     */
    static enum Type {LINES_OF_CODE, HALSTEAD, CYCLOMATIC}
}
