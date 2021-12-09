package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMetric implements Metric {
    static enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD}

    private String id;

    protected final Map<PsiClass, Map<PsiMethod, MetricValue>> metrics;

    public BaseMetric() {
        metrics = new HashMap<>();
    }

    public abstract double calculate();

    public abstract double get();

    public abstract Double get(PsiClass psiClass, PsiMethod psiMethod);

    public abstract Map<PsiClass, Map<PsiMethod, Double>> getMetrics();

    public void setMetric(double metric, PsiClass psiClass, PsiMethod psiMethod, String type) {
        if (!metrics.containsKey(psiClass)) {
            metrics.put(psiClass, new HashMap<>());
        }

        if(!metrics.get(psiClass).containsKey(psiMethod)) {
            metrics.get(psiClass).put(psiMethod, new MetricValue(getID(), psiClass.getName()));
        }

        metrics.get(psiClass).get(psiMethod).set(type, metric);
    }

    public String getID() {
        return this.id;
    }

    protected void setID(String id) {
        this.id = id;
    }

    public abstract boolean checkDegradation();

    public void save() {
        //TODO: Implement save
    }
}
