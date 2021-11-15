package org.csed332.project.team2.metrics;

public interface Metric {
    double get();
    double calculate();

    // for db
    String getID();
}
