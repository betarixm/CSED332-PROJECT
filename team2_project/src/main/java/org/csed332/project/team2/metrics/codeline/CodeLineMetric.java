package org.csed332.project.team2.metrics.codeline;

import org.csed332.project.team2.metrics.Metric;

public abstract class CodeLineMetric implements Metric {
    private int codeLine;

    public CodeLineMetric() {

    }

    @Override
    public double get() {
        return this.codeLine;
    }

    protected void set(int codeLine) {
        this.codeLine = codeLine;
    }

    public String getID() {
        return "code-line";
    }

}