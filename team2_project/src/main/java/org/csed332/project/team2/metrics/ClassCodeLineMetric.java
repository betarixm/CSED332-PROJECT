package org.csed332.project.team2.metrics;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;

import com.intellij.psi.*;

public class ClassCodeLineMetric extends CodeLineMetric {
    public String className;

    PsiClass psiClass;

    public ClassCodeLineMetric(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public double calculate() {
        return psiClass.getText().split("\n").length;
    }

    @Override
    public double get() {
        List<MetricModel> metricModelList = MetricModelService.getMetrics(getID(), className, 1);

        return metricModelList.isEmpty() ? super.get() : metricModelList.get(0).getFigure();
    }

    @Override
    protected void set(int value) {
        super.set(value);
        MetricModelService.saveMetric(getID(), className, value);
    }

}