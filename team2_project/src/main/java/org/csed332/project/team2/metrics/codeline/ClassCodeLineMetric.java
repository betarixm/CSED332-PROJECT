package org.csed332.project.team2.metrics.codeline;

import com.intellij.psi.PsiClass;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.utils.WarningCondition;

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