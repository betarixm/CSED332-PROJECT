package org.csed332.project.team2.metrics;


public interface Metric {
    double get();

    double calculate();

    String getID();

    boolean checkDegradation();

    void save();

    static enum Type {LINES_OF_CODE, HALSTEAD, CYCLOMATIC}

}
