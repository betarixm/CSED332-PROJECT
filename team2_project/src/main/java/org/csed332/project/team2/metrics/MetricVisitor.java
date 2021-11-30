package org.csed332.project.team2.metrics;

import com.intellij.psi.*;

public abstract class MetricVisitor extends BaseMetric {
    private double visitResult = 0;
    private PsiElement psiElement;

    public MetricVisitor(PsiElement element) {
        setPsiElement(element);
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public void setPsiElement(PsiElement element) {
        psiElement = element;
    }

    protected abstract void visitPackageMetric(PsiPackage pack);

    protected abstract void visitClassMetric(PsiClass aClass);

    protected abstract void visitMethodMetric(PsiMethod method);

    protected abstract void visitFieldMetric(PsiField field);

    protected void setVisitResult(double result) {
        visitResult = result;
    }

    protected double getVisitResult() {
        return visitResult;
    }

    protected double visit(PsiElement psiElement) {
        final var visitor = new JavaElementVisitor() {
            @Override
            public void visitPackage(PsiPackage pack) {
                visitPackageMetric(pack);
            }

            @Override
            public void visitClass(PsiClass aClass) {
                visitClassMetric(aClass);
            }

            @Override
            public void visitMethod(PsiMethod method) {
                visitMethodMetric(method);
            }

            @Override
            public void visitField(PsiField field) {
                visitFieldMetric(field);
            }
        };

        psiElement.accept(visitor);

        return getVisitResult();
    }

    @Override
    public double calculate() {
        setMetric(visit(this.psiElement));
        return getMetric();
    }
}
