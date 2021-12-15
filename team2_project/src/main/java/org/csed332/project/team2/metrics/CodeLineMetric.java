package org.csed332.project.team2.metrics;

public abstract class CodeLineMetric implements Metric {
    private int codeLine;

    public CodeLineMetric() {
        // TODO: how to test ProjectCodeLine?
        //  Now, getProject() returns just temp project..

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