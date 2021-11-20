package org.csed332.project.team2.metrics.coverage;

import java.util.ArrayList;

public abstract class CompositeCodeCoverageMetric extends CodeCoverageMetric {
    ArrayList<CodeCoverageMetric> subMetrics;

    public CompositeCodeCoverageMetric() {
        subMetrics = new ArrayList<>();
    }

    public void addMetric(CodeCoverageMetric codeCoverageMetric) {
        subMetrics.add(codeCoverageMetric);
    }

    @Override
    public double calculate() {
        totalStatementsCount = 0;
        coveredStatementsCount = 0;

        for (CodeCoverageMetric subMetric : subMetrics) {
            subMetric.calculate();

            totalStatementsCount += subMetric.totalStatementsCount;
            coveredStatementsCount += subMetric.coveredStatementsCount;
        }

        return get();
    }
}
