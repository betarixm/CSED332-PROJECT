package org.csed332.project.team2.metrics;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import org.apache.tools.ant.taskdefs.War;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.MetricModel;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseMetric implements Metric {
    protected enum Type {LINES_OF_CODE, CYCLOMATIC, HALSTEAD}

    private double metric;
    private String id;
    protected WarningCondition cond;

    private final Map<PsiClass, Map<PsiMethod, Double>> metrics;

    public void setCondition(WarningCondition cond){
        this.cond = cond;
    }

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
        // TODO? get from DB
        return null;
    }

    public void setWarningCondition(WarningCondition cond){
        this.cond = cond;
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

        // TODO? set to DB
        //  use MetricService.addMetric(...)
    }

    public String getID() {
        return this.id;
    }

    protected void setID(String id) {
        this.id = id;
    }

    public Map<PsiClass, Set<PsiMethod>> getDegradationMetrics(){
        Map<PsiClass, Set<PsiMethod>> degradedMetrics = new HashMap<>();

        Collection<MetricModel> metricModels = MetricService.query(getID(), 1).get(0).getMetricModels();
        for(PsiClass psiClass : metrics.keySet()){
            for(PsiMethod psiMethod : metrics.get(psiClass).keySet()){
                List<MetricModel> subMetricModels = metricModels.stream()
                        .filter(m -> m.getClassName().equals(psiClass.getName())
                                && m.getMethodName().equals(psiMethod.getName()))
                        .collect(Collectors.toList());

                if(subMetricModels.size() < 2) continue;

                Double newValue = subMetricModels.get(1).getFigure();
                Double oldValue = subMetricModels.get(0).getFigure();

                if(cond.shouldWarn(oldValue, newValue)){
                    degradedMetrics.putIfAbsent(psiClass, new HashSet<>());
                    degradedMetrics.get(psiClass).add(psiMethod);
                }
            }
        }

        return Collections.unmodifiableMap(degradedMetrics);
    }

    @Override
    public boolean checkDegradation() {
        //TODO: make this method return true if value of Halstead Metric degraded.
        return !getDegradationMetrics().isEmpty();
    }

    // save metrics to DB
    public abstract void save(CalcHistoryModel calc);

    @Override
    public void save() {}
}
