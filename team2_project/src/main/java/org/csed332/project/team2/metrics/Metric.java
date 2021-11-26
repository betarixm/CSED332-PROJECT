package org.csed332.project.team2.metrics;


public interface Metric {
    static enum Type {LINES_OF_CODE} // TODO: add other types once implemented HALSTED, CYCLO, INDEX, COVERAGE

    double get();

    double calculate();

    String getID();
}
