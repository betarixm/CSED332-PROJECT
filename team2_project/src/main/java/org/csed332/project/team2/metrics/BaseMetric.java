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

/**
 * Abstract class for metric.
 */
public abstract class BaseMetric implements Metric {
    private final Map<PsiClass, Map<PsiMethod, Double>> metrics;
    protected WarningCondition cond;
    private double metric;
    private String id;

    /**
     * Instantiates a new BaseMetric.
     */
    public BaseMetric() {
        metrics = new HashMap<>();
        this.cond = new WarningCondition(WarningCondition.Mode.NO_WARNING);
    }

    /**
     * Sets condition.
     *
     * @param cond the condition
     */
    public void setCondition(WarningCondition cond) {
        this.cond = cond;
    }

    public abstract double calculate();

    public double get() {
        return this.metric;
    }

    /**
     * Get figure of metric with given class and method.
     * Return null if there's no matching class and method.
     *
     * @param psiClass  the PsiClass
     * @param psiMethod the PsiMethod
     * @return the figure
     */
    public Double get(PsiClass psiClass, PsiMethod psiMethod) {
        if (metrics.containsKey(psiClass)) {
            return metrics.get(psiClass).get(psiMethod);
        }
        return null;
    }

    /**
     * Sets warning condition.
     *
     * @param cond the condition
     */
    public void setWarningCondition(WarningCondition cond) {
        this.cond = cond;
    }

    /**
     * Gets metrics data.
     *
     * @return the metrics data
     */
    public Map<PsiClass, Map<PsiMethod, Double>> getMetrics() {
        return Collections.unmodifiableMap(metrics);
    }

    /**
     * Sets overall metric data.
     *
     * @param metric the metric data
     */
    public void setMetric(double metric) {
        this.metric = metric;
    }

    /**
     * Sets specific metric data.
     *
     * @param metric    the metric value(figure)
     * @param psiClass  the PsiClass
     * @param psiMethod the PsiMethod
     */
    public void setMetric(double metric, PsiClass psiClass, PsiMethod psiMethod) {
        if (!metrics.containsKey(psiClass)) {
            metrics.put(psiClass, new HashMap<>());
        }

        metrics.get(psiClass).put(psiMethod, metric);
    }

    public String getID() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    protected void setID(String id) {
        this.id = id;
    }

    /**
     * Gets map of classes and methods that metric has degraded on.
     *
     * @return the map of classes and methods
     */
    public Map<PsiClass, Set<PsiMethod>> getDegradationMetrics() {
        Map<PsiClass, Set<PsiMethod>> degradedMetrics = new HashMap<>();

        Collection<MetricModel> metricModels = MetricService.query(getID(), 1).get(0).getMetricModels();
        for (PsiClass psiClass : metrics.keySet()) {
            for (PsiMethod psiMethod : metrics.get(psiClass).keySet()) {
                String halsteadType = this instanceof HalsteadMetric
                        ? ((HalsteadMetric) this).getType() : "";
                List<MetricModel> subMetricModels = metricModels.stream()
                        .filter(m -> m.getClassName().equals(psiClass.getName())
                                && m.getMethodName().equals(psiMethod.getName())
                                && m.getType().equals(halsteadType))
                        .collect(Collectors.toList());

                Double newValue = metrics.get(psiClass).get(psiMethod);
                Double oldValue;
                if (subMetricModels.isEmpty()) {
                    oldValue = newValue;
                } else oldValue = subMetricModels.get(0).getFigure();

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

    /**
     * Save metrics data to database.
     *
     * @param calc the CalcHistoryModel object to group metrics data
     */
    public abstract void save(CalcHistoryModel calc);

    @Override
    public void save() {
    }

    /**
     * The enum Type.
     */
    protected enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD}
}
