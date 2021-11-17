package org.csed332.project.team2.metrics;

public abstract class CodeLineMetric implements Metric {
    private int codeLine;
    protected String path;

    public CodeLineMetric(String path) {
        setPath(path);
    }

    @Override
    public double get() {
        return this.codeLine;
    }

    protected void set(int codeLine) {
        this.codeLine = codeLine;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public String getID() {
        return null;
    }

    // for test
    public String getPath() {
        return path;
    }
}