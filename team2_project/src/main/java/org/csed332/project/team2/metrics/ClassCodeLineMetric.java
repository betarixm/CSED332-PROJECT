package org.csed332.project.team2.metrics;

import org.apache.tools.ant.taskdefs.War;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.WarningCondition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;

import com.intellij.psi.*;

public class ClassCodeLineMetric extends CodeLineMetric {

    private final PsiClass psiClass;
    private WarningCondition cond;

    public ClassCodeLineMetric(PsiClass psiClass) {
        this.psiClass = psiClass;
        this.cond = new WarningCondition(WarningCondition.Mode.MORE_THAN, 100);
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
        return cond.shouldWarn(0, get());
    }

    @Override
    public void save() {
        MetricModelService.saveMetric(getID(), psiClass.getName(), get());
    }
}