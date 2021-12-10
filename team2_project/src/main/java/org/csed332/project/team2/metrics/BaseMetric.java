package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMetric implements Metric {
    static enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD}

    private double metric;
    private String id;
    protected WarningCondition cond;

    private final Map<PsiClass, Map<PsiMethod, Double>> metrics;

    public BaseMetric() {
        metrics = new HashMap<>();
        this.cond = new WarningCondition(WarningCondition.Mode.MORE_THAN, 5);
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

    @Override
    public boolean checkDegradation() {
        //TODO: make this method return true if value of Halstead Metric degraded.
    }

    public void save() {
        //TODO: Implement save
    }
}
