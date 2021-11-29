package org.csed332.project.team2.metrics.cyclomatic;

import com.intellij.psi.PsiMethod;

public class CyclomaticMethodMetric extends CyclomaticMetric {
    PsiMethod method;

    public CyclomaticMethodMetric(PsiMethod method) {
        this.method = method;
    }

    @Override
    public double calculate() {
        return 0;
    }
}
