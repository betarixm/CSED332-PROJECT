package org.csed332.project.team2.metrics.cyclomatic;

import com.intellij.psi.*;
import org.csed332.project.team2.metrics.VisitingMetric;

import java.util.Objects;

public class CyclomaticMetric extends VisitingMetric {
    public CyclomaticMetric(PsiElement element) {
        super(element);
        setId("cyclomatic");
    }

    @Override
    protected void visitPackageMetric(PsiPackage pack) {
        for (PsiClass aClass : pack.getClasses()) {
            aClass.accept(visitor);
        }
    }

    @Override
    protected void visitClassMetric(PsiClass aClass) {
        for (PsiMethod method : aClass.getMethods()) {
            double value = getVisitResult();
            method.accept(visitor);
            setMetric(getVisitResult() - value, aClass, method);
        }
    }

    @Override
    protected void visitMethodMetric(PsiMethod method) {
        PsiCodeBlock codes = method.getBody();

        if (codes != null) {
            for (PsiStatement statement : codes.getStatements()) {
                statement.accept(visitor);
            }
        }

    }

    @Override
    protected void visitFieldMetric(PsiField field) {

    }

    @Override
    protected void visitBranchMetric(PsiConditionalExpression expr) {
        setVisitResult(getVisitResult() + 1);

        expr.getCondition().accept(visitor);
        Objects.requireNonNull(expr.getThenExpression()).accept(visitor);
        Objects.requireNonNull(expr.getElseExpression()).accept(visitor);
    }

    @Override
    protected void visitSwitchMetric(PsiSwitchLabelStatement sLabel) {
        if (!sLabel.isDefaultCase()) {
            setVisitResult(getVisitResult() + 1);
        }

        Objects.requireNonNull(sLabel.getEnclosingSwitchBlock()).accept(visitor);
    }
}
