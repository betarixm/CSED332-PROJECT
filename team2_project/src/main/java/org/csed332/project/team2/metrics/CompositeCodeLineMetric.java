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

    public CompositeCodeLineMetric(String path) {
        super(path);
        codeLineMetrics = new ArrayList<>();

        String absolutePath = new File(path).getAbsolutePath();
        String[] subPathList = new File(absolutePath).list();

        for (String subPath : subPathList) {
            String absoluteSubPath = path + "/" + subPath;
            File sub = new File(absoluteSubPath);
            CodeLineMetric subMetric = sub.isDirectory() ?
                    new PackageCodeLineMetric(absoluteSubPath) : new ClassCodeLineMetric(absoluteSubPath);
            addMetric(subMetric);
        }
    }

    public double calculate() {
        int totalLines = 0;
        for (CodeLineMetric metric : codeLineMetrics) {
            totalLines += metric.calculate();
        }
        return totalLines;
    }

    public List<CodeLineMetric> getCodeLineMetrics() {
        return Collections.unmodifiableList(codeLineMetrics);
    }

    public String getID() {
        return "0000-0000-0000-0000";
    }
}
