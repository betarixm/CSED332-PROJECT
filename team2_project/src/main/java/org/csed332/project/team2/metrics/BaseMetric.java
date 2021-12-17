package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.csed332.project.team2.utils.WarningCondition;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseMetric implements Metric {
    private final Map<String, Map<PsiMethod, Double>> metrics;
    protected WarningCondition cond;
    private double metric;
    private String id;

    public BaseMetric() {
        metrics = new HashMap<>();
        this.cond = new WarningCondition(WarningCondition.Mode.NO_WARNING);
    }

    public void setCondition(WarningCondition cond) {
        this.cond = cond;
    }

    public abstract double calculate();

    public double get() {
        return this.metric;
    }

    public Double get(PsiClass psiClass, PsiMethod psiMethod) {
        String aClass = psiClass.getName();
        if (metrics.containsKey(aClass)) {
            return metrics.get(aClass).get(psiMethod);
        }
        return null;
    }

    public void setWarningCondition(WarningCondition cond) {
        this.cond = cond;
    }

    public Map<String, Map<PsiMethod, Double>> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    public void setMetric(double metric) {
        this.metric = metric;
    }

    public void setMetric(double metric, String aClass, PsiMethod psiMethod) {
        if (!metrics.containsKey(aClass)) {
            metrics.put(aClass, new HashMap<>());
        }

        metrics.get(aClass).put(psiMethod, metric);

    }

    public String getID() {
        return this.id;
    }

    protected void setID(String id) {
        this.id = id;
    }

    public Map<String, Set<PsiMethod>> getDegradationMetrics() {
        Map<String, Set<PsiMethod>> degradedMetrics = new HashMap<>();

        Collection<MetricModel> metricModels = MetricService.query(getID(), 1).get(0).getMetricModels();
        for (String psiClass : metrics.keySet()) {
            for (PsiMethod psiMethod : metrics.get(psiClass).keySet()) {
                String halsteadType = this instanceof HalsteadMetric
                        ? ((HalsteadMetric) this).getType() : "";
                List<MetricModel> subMetricModels = metricModels.stream()
                        .filter(m -> m.getClassName().equals(psiClass)
                                && m.getMethodName().equals(psiMethod.getName())
                                && m.getType().equals(halsteadType))
                        .collect(Collectors.toList());

                Double newValue = metrics.get(psiClass).get(psiMethod);
                Double oldValue;
                if (subMetricModels.isEmpty()) {
                    oldValue = newValue;
                } else {
                    oldValue = subMetricModels.get(0).getFigure();
                }

                if (cond.shouldWarn(oldValue, newValue)) {
                    degradedMetrics.putIfAbsent(psiClass, new HashSet<>());
                    degradedMetrics.get(psiClass).add(psiMethod);
                }
            }
        }

        return Collections.unmodifiableMap(degradedMetrics);
    }

    @Override
    public boolean checkDegradation() {
        return !getDegradationMetrics().isEmpty();
    }

    public abstract void save(CalcHistoryModel calc);

    @Override
    public void save() {
    }

    protected enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD, MAINTAINABILITY}
}
