package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMetric implements Metric {
    static enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD}

    private double metric;
    private String id;

    private final Map<PsiClass, Map<PsiMethod, Double>> metrics;

    public BaseMetric() {
        metrics = new HashMap<>();
    }

    public abstract double calculate();

    public double get() {
        return this.metric;
    }

    public Double get(PsiClass psiClass, PsiMethod psiMethod) {
        if (metrics.containsKey(psiClass)) {
            return metrics.get(psiClass).get(psiMethod);
        }

        return null;
    }

    public Map<PsiClass, Map<PsiMethod, Double>> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    public void setMetric(double metric) {
        this.metric = metric;
    }

    public void setMetric(double metric, PsiClass psiClass, PsiMethod psiMethod) {
        if (!metrics.containsKey(psiClass)) {
            metrics.put(psiClass, new HashMap<>());
        }

        metrics.get(psiClass).put(psiMethod, metric);
    }

    public String getID() {
        return this.id;
    }

    protected void setID(String id) {
        this.id = id;
    }
}
