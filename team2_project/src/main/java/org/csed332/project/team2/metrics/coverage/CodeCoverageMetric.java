package org.csed332.project.team2.metrics.coverage;

import org.csed332.project.team2.metrics.Metric;

public abstract class CodeCoverageMetric implements Metric {
    protected int totalStatementsCount, coveredStatementsCount;

    @Override
    public double get() {
        return ((double) coveredStatementsCount) / ((double) totalStatementsCount);
    }

    @Override
    public String getID() {
        return null;
    }
}
