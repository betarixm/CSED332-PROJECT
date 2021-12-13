package org.csed332.project.team2.metrics;


public interface Metric {
    static enum Type {LINES_OF_CODE, HALSTEAD, CYCLOMATIC}

    double get();

    double calculate();

    String getID();

    /*
     * Compare current metric value with most recent one.
     * If metric value degraded, return true. Otherwise, return false.
     */
    boolean checkDegradation();

    void save();
    // void setWarningCondition();
}
