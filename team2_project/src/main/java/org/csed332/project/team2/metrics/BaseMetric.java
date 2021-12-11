package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import java.util.*;

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

    public Map<PsiClass, Set<PsiMethod>> getDegradationMetrics(){
        Map<PsiClass, Set<PsiMethod>> degradedMetrics = new HashMap<>();

        for(Map.Entry<PsiClass, Map<PsiMethod, Double>> classEntry : metrics.entrySet()){
            PsiClass eachClass = classEntry.getKey();
            for(PsiMethod m: classEntry.getValue().keySet()){
                String savingName = eachClass.getName() + "@" + m.getName();

                List<MetricModel> metricModels = MetricModelService.getMetrics(getID(), savingName, 2);

                Double olderValue = metricModels.get(1).getFigure();
                Double newValue = metricModels.get(0).getFigure();
                if(metricModels.size() >= 2 && cond.shouldWarn(olderValue, newValue)){
                    degradedMetrics.putIfAbsent(eachClass, new HashSet<>());
                    degradedMetrics.get(eachClass).add(m);
                }
            }
        }

        return Collections.unmodifiableMap(degradedMetrics);
    }

    @Override
    public boolean checkDegradation() {
        //TODO: make this method return true if value of Halstead Metric degraded.
        return false;
    }

    public void save() {
        //TODO: Implement save
        for(Map.Entry<PsiClass, Map<PsiMethod, Double>> classEntry : metrics.entrySet()){
            PsiClass eachClass = classEntry.getKey();
            for(PsiMethod m: classEntry.getValue().keySet()){
                String savingName = eachClass.getName() + "@" + m.getName();

                MetricModelService.saveMetric(getID(), savingName, get(eachClass, m));
            }
        }
    }
}
