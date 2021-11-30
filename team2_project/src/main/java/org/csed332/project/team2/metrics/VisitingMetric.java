package org.csed332.project.team2.metrics;

import com.intellij.ide.projectView.impl.nodes.PackageUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public abstract class VisitingMetric extends BaseMetric {
    private double visitResult = 0;
    private PsiElement psiElement;
    private Project project;

    final public PsiElementVisitor visitor = new JavaElementVisitor() {
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

        @Override
        public void visitConditionalExpression(PsiConditionalExpression expr) {
            visitBranchMetric(expr);
        }

        @Override
        public void visitAssertStatement(PsiAssertStatement statement) {
            visitAssertStatementMetric(statement);
        }

        @Override
        public void visitBlockStatement(PsiBlockStatement statement) {
            visitBlockStatementMetric(statement);
        }

        @Override
        public void visitBreakStatement(PsiBreakStatement statement) {
            visitBreakStatementMetric(statement);
        }

        @Override
        public void visitYieldStatement(PsiYieldStatement statement) {
            visitYieldStatementMetric(statement);
        }

        @Override
        public void visitContinueStatement(PsiContinueStatement statement) {
            visitContinueStatementMetric(statement);
        }

        @Override
        public void visitDeclarationStatement(PsiDeclarationStatement statement) {
            visitDeclarationStatementMetric(statement);
        }

        @Override
        public void visitDoWhileStatement(PsiDoWhileStatement statement) {
            visitDoWhileStatementMetric(statement);
        }

        @Override
        public void visitEmptyStatement(PsiEmptyStatement statement) {
            visitEmptyStatementMetric(statement);
        }

        @Override
        public void visitExpressionListStatement(PsiExpressionListStatement statement) {
            visitExpressionListStatementMetric(statement);
        }

        @Override
        public void visitExpressionStatement(PsiExpressionStatement statement) {
            visitExpressionStatementMetric(statement);
        }

        @Override
        public void visitForStatement(PsiForStatement statement) {
            visitForStatementMetric(statement);
        }

        @Override
        public void visitForeachStatement(PsiForeachStatement statement) {
            visitForeachStatementMetric(statement);
        }

        @Override
        public void visitIfStatement(PsiIfStatement statement) {
            visitIfStatementMetric(statement);
        }

        @Override
        public void visitImportStatement(PsiImportStatement statement) {
            visitImportStatementMetric(statement);
        }

        @Override
        public void visitImportStaticStatement(PsiImportStaticStatement statement) {
            visitImportStaticStatementMetric(statement);
        }

        @Override
        public void visitLabeledStatement(PsiLabeledStatement statement) {
            visitLabeledStatementMetric(statement);
        }

        @Override
        public void visitPackageStatement(PsiPackageStatement statement) {
            visitPackageStatementMetric(statement);
        }

        @Override
        public void visitReturnStatement(PsiReturnStatement statement) {
            visitReturnStatementMetric(statement);
        }

        @Override
        public void visitStatement(PsiStatement statement) {
            visitStatementMetric(statement);
        }

        @Override
        public void visitSwitchLabelStatement(PsiSwitchLabelStatement statement) {
            visitSwitchLabelStatementMetric(statement);
        }

        @Override
        public void visitSwitchLabeledRuleStatement(PsiSwitchLabeledRuleStatement statement) {
            visitSwitchLabeledRuleStatementMetric(statement);
        }

        @Override
        public void visitSwitchStatement(PsiSwitchStatement statement) {
            visitSwitchStatementMetric(statement);
        }

        @Override
        public void visitSynchronizedStatement(PsiSynchronizedStatement statement) {
            visitSynchronizedStatementMetric(statement);
        }

        @Override
        public void visitThrowStatement(PsiThrowStatement statement) {
            visitThrowStatementMetric(statement);
        }

        @Override
        public void visitTryStatement(PsiTryStatement statement) {
            visitTryStatementMetric(statement);
        }

        @Override
        public void visitWhileStatement(PsiWhileStatement statement) {
            visitWhileStatementMetric(statement);
        }

        @Override
        public void visitModuleStatement(PsiStatement statement) {
            visitModuleStatementMetric(statement);
        }

        @Override
        public void visitRequiresStatement(PsiRequiresStatement statement) {
            visitRequiresStatementMetric(statement);
        }

        @Override
        public void visitPackageAccessibilityStatement(PsiPackageAccessibilityStatement statement) {
            visitPackageAccessibilityStatementMetric(statement);
        }

        @Override
        public void visitUsesStatement(PsiUsesStatement statement) {
            visitUsesStatementMetric(statement);
        }

        @Override
        public void visitProvidesStatement(PsiProvidesStatement statement) {
            visitProvidesStatementMetric(statement);
        }
    };

    public VisitingMetric(PsiElement element) {
        setPsiElement(element);
    }

    public VisitingMetric(Project project) {
        setProject(project);
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public void setPsiElement(PsiElement element) {
        psiElement = element;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    protected abstract void visitPackageMetric(PsiPackage pack);

    protected abstract void visitClassMetric(PsiClass aClass);

    protected abstract void visitMethodMetric(PsiMethod method);

    protected abstract void visitFieldMetric(PsiField field);

    protected abstract void visitBranchMetric(PsiConditionalExpression expr);

    protected abstract void visitAssertStatementMetric(PsiAssertStatement statement);

    protected abstract void visitBlockStatementMetric(PsiBlockStatement statement);

    protected abstract void visitBreakStatementMetric(PsiBreakStatement statement);

    protected abstract void visitYieldStatementMetric(PsiYieldStatement statement);

    protected abstract void visitContinueStatementMetric(PsiContinueStatement statement);

    protected abstract void visitDeclarationStatementMetric(PsiDeclarationStatement statement);

    protected abstract void visitDoWhileStatementMetric(PsiDoWhileStatement statement);

    protected abstract void visitEmptyStatementMetric(PsiEmptyStatement statement);

    protected abstract void visitExpressionListStatementMetric(PsiExpressionListStatement statement);

    protected abstract void visitExpressionStatementMetric(PsiExpressionStatement statement);

    protected abstract void visitForStatementMetric(PsiForStatement statement);

    protected abstract void visitForeachStatementMetric(PsiForeachStatement statement);

    protected abstract void visitIfStatementMetric(PsiIfStatement statement);

    protected abstract void visitImportStatementMetric(PsiImportStatement statement);

    protected abstract void visitImportStaticStatementMetric(PsiImportStaticStatement statement);

    protected abstract void visitLabeledStatementMetric(PsiLabeledStatement statement);

    protected abstract void visitPackageStatementMetric(PsiPackageStatement statement);

    protected abstract void visitReturnStatementMetric(PsiReturnStatement statement);

    protected abstract void visitStatementMetric(PsiStatement statement);

    protected abstract void visitSwitchLabelStatementMetric(PsiSwitchLabelStatement statement);

    protected abstract void visitSwitchLabeledRuleStatementMetric(PsiSwitchLabeledRuleStatement statement);

    protected abstract void visitSwitchStatementMetric(PsiSwitchStatement statement);

    protected abstract void visitSynchronizedStatementMetric(PsiSynchronizedStatement statement);

    protected abstract void visitThrowStatementMetric(PsiThrowStatement statement);

    protected abstract void visitTryStatementMetric(PsiTryStatement statement);

    protected abstract void visitWhileStatementMetric(PsiWhileStatement statement);

    protected abstract void visitModuleStatementMetric(PsiStatement statement);

    protected abstract void visitRequiresStatementMetric(PsiRequiresStatement statement);

    protected abstract void visitPackageAccessibilityStatementMetric(PsiPackageAccessibilityStatement statement);

    protected abstract void visitUsesStatementMetric(PsiUsesStatement statement);

    protected abstract void visitProvidesStatementMetric(PsiProvidesStatement statement);

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

        return get();
    }
}
