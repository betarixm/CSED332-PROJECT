package org.csed332.project.team2.metrics.cyclomatic;

import com.intellij.psi.*;
import org.csed332.project.team2.metrics.MetricVisitor;

public class CyclomaticMetric extends MetricVisitor {
    public CyclomaticMetric(PsiElement element) {
        super(element);
        setId("cyclomatic");
    }

    @Override
    protected void visitPackageMetric(PsiPackage pack) {

    }

    @Override
    protected void visitClassMetric(PsiClass aClass) {

    }

    @Override
    protected void visitMethodMetric(PsiMethod method) {

    }

    @Override
    protected void visitFieldMetric(PsiField field) {

    }
}
