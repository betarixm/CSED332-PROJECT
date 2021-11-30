package org.csed332.project.team2.metrics;

public abstract class BaseMetric {
    static enum Type {LINES_OF_CODE, CYCLOMATIC}

    private double metric;
    private String id;

    public abstract double calculate();

    public double getMetric() {
        return this.metric;
    }

    public void setMetric(double metric) {
        this.metric = metric;
    }

    public String getId() {
        return this.id;
    }

    protected void setId(String id) {
        this.id = id;
    }
}
