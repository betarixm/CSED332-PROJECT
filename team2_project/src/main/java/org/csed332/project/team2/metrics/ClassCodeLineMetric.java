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

    PsiClass psiClass;

    public ClassCodeLineMetric(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public double calculate() {
        int codeLine = psiClass.getText().split("\n").length;
        set(codeLine);
        return codeLine;
    }

    @Override
    public double get() {
        return super.get();
    }

    @Override
    protected void set(int value) {
        super.set(value);
    }

    @Override
    public boolean checkDegradation() {
        List<MetricModel> metricModelList = MetricModelService.getMetrics(getID(), psiClass.getName(), 2);

        if (metricModelList.size() == 2) {
            return (metricModelList.get(0).getFigure() > metricModelList.get(1).getFigure());
        } else return false;
    }

    @Override
    public void save() {
        MetricModelService.saveMetric(getID(), psiClass.getName(), get());
    }
}