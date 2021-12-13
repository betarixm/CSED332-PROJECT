package org.csed332.project.team2.metrics;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

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

        List<CalcHistoryModel> query = MetricService.query(getID(), 2);
        query.stream().filter(calcHistoryModel -> {
            List<MetricModel> metricModels = new ArrayList<>(calcHistoryModel.getMetricModels());
            if(metricModels.size() < 2) return false;

            Double olderValue = metricModels.get(0).getFigure();
            Double newValue = metricModels.get(1).getFigure();

            return cond.shouldWarn(olderValue, newValue);
        }).forEach(calcHistoryModel -> {
            MetricModel metricModel = calcHistoryModel.getMetricModels().iterator().next();
            String className = metricModel.getClassName();
            String methodName = metricModel.getMethodName();

            PsiClass psiClass = metrics.keySet().stream()
                    .filter(aClass -> aClass.getName().equals(className))
                    .findFirst().get();

            PsiMethod psiMethod = metrics.get(psiClass).keySet().stream()
                    .filter(aMethod -> aMethod.getName().equals(methodName))
                    .findFirst().get();

            degradedMetrics.putIfAbsent(psiClass, new HashSet<>());
            degradedMetrics.get(psiClass).add(psiMethod);
        });

        return Collections.unmodifiableMap(degradedMetrics);
    }

    @Override
    public boolean checkDegradation() {
        //TODO: make this method return true if value of Halstead Metric degraded.
        return false;
    }

    // save metrics to DB
    public abstract void save(CalcHistoryModel calc);

    @Override
    public void save() {}
}
