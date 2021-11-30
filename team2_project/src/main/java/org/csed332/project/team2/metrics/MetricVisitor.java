package org.csed332.project.team2.metrics;

import com.intellij.ide.projectView.impl.nodes.PackageUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public abstract class MetricVisitor extends BaseMetric {
    private double visitResult = 0;
    private PsiElement psiElement;
    private Project project;

    final PsiElementVisitor visitor = new JavaElementVisitor() {
        @Override
        public void visitDirectory(@NotNull PsiDirectory dir) {
            final var psiPackage = JavaDirectoryService.getInstance().getPackage(dir);
            if (psiPackage != null && !PackageUtil.isPackageDefault(psiPackage)) {
                psiPackage.accept(this);
            } else {
                Arrays.stream(dir.getSubdirectories()).forEach(sd -> sd.accept(this));
            }
        }

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
        psiElement.accept(visitor);
        return getVisitResult();
    }

    protected double visit(Project project) {
        var rootManager = ProjectRootManager.getInstance(project);
        var psiManager = PsiManager.getInstance(project);

        Arrays.stream(rootManager.getContentSourceRoots())
                .map(psiManager::findDirectory)
                .filter(Objects::nonNull)
                .forEach(dir -> dir.accept(visitor));

        return getVisitResult();
    }

    @Override
    public double calculate() {
        if (psiElement != null) {
            setMetric(visit(psiElement));
        } else if (project != null) {
            setMetric(visit(project));
        }

        return getMetric();
    }
}
