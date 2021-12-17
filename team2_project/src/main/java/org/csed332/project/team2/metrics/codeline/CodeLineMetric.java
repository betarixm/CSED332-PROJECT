package org.csed332.project.team2.metrics.codeline;

import org.csed332.project.team2.metrics.Metric;

/**
 * Class for code line metric.
 */
public abstract class CodeLineMetric implements Metric {
    private int codeLine;

    /**
     * Instantiates a new CodeLineMetric.
     */
    public CodeLineMetric() {}

    @Override
    public double get() {
        return this.codeLine;
    }

    /**
     * Set lines of code.
     *
     * @param codeLine the code line
     */
    protected void set(int codeLine) {
        this.codeLine = codeLine;
    }

    public String getID() {
        return "code-line";
    }

}