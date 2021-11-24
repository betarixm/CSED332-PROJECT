package org.csed332.project.team2.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CompositeCodeLineMetric extends CodeLineMetric {
    private List<CodeLineMetric> codeLineMetrics;

    public void addMetric(CodeLineMetric codeLineMetric) {
        codeLineMetrics.add(codeLineMetric);
    }

    public CompositeCodeLineMetric() {
        codeLineMetrics = new ArrayList<>();
    }

    public double calculate() {
        int line = (int) codeLineMetrics.stream().mapToDouble(m -> m.calculate()).sum();
        set(line);
        return line;
    }

    public List<CodeLineMetric> getCodeLineMetrics() {
        return Collections.unmodifiableList(codeLineMetrics);
    }

}
