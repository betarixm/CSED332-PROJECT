package org.csed332.project.team2.metrics.codeline;

import org.csed332.project.team2.metrics.Metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite class for code line metric.
 */
public abstract class CompositeCodeLineMetric extends CodeLineMetric {
    private List<CodeLineMetric> codeLineMetrics;

    /**
     * Instantiates a new CompositeCodeLineMetric.
     */
    public CompositeCodeLineMetric() {
        codeLineMetrics = new ArrayList<>();
    }

    /**
     * Add a CodeLineMetric.
     *
     * @param codeLineMetric the CodeLineMetric to be added.
     */
    public void addMetric(CodeLineMetric codeLineMetric) {
        codeLineMetrics.add(codeLineMetric);
    }

    public double calculate() {
        int line = (int) codeLineMetrics.stream().mapToDouble(m -> m.calculate()).sum();
        set(line);
        return line;
    }

    /**
     * Gets list of CodeLineMetric.
     *
     * @return the list of CodeLineMetric
     */
    public List<CodeLineMetric> getCodeLineMetrics() {
        return Collections.unmodifiableList(codeLineMetrics);
    }

    @Override
    public boolean checkDegradation() {
        return codeLineMetrics.stream().anyMatch(Metric::checkDegradation);
    }

    @Override
    public void save() {
        for (CodeLineMetric c : codeLineMetrics) {
            c.save();
        }
    }
}
