package org.csed332.project.team2.metrics;

import com.intellij.ide.projectView.impl.nodes.PackageUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Visitor class for BaseMetric.
 */
public abstract class VisitingMetric extends BaseMetric {
    /**
     * The Visitor.
     */
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

        @Override
        public void visitLocalVariable(PsiLocalVariable variable) {
            visitLocalVariableMetric(variable);
        }

        @Override
        public void visitAssignmentExpression(PsiAssignmentExpression expr) {
            visitAssignmentExpressionMetric(expr);
        }

        @Override
        public void visitParenthesizedExpression(PsiParenthesizedExpression expr) {
            visitParenthesizedExpressionMetric(expr);
        }

        @Override
        public void visitPolyadicExpression(PsiPolyadicExpression expr) {
            visitPolyadicExpressionMetric(expr);
        }

        @Override
        public void visitUnaryExpression(PsiUnaryExpression expr) {
            visitUnaryExpressionMetric(expr);
        }
    };
    private double visitResult = 0;
    private PsiElement psiElement;
    private Project project;

    /**
     * Instantiates a new VisitingMetric.
     *
     * @param element the PsiElement
     */
    public VisitingMetric(PsiElement element) {
        setPsiElement(element);
    }

    /**
     * Instantiates a new VisitingMetric.
     *
     * @param project the Project
     */
    public VisitingMetric(Project project) {
        setProject(project);
    }

    /**
     * Gets PsiElement.
     *
     * @return the PsiElement
     */
    public PsiElement getPsiElement() {
        return psiElement;
    }

    /**
     * Sets PsiElement.
     *
     * @param element the PsiElement
     */
    public void setPsiElement(PsiElement element) {
        psiElement = element;
    }

    /**
     * Gets Project.
     *
     * @return the Project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets Project.
     *
     * @param project the Project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Visit package.
     *
     * @param pack the PsiPackage
     */
    protected abstract void visitPackageMetric(PsiPackage pack);

    /**
     * Visit class.
     *
     * @param aClass the PsiClass
     */
    protected abstract void visitClassMetric(PsiClass aClass);

    /**
     * Visit method.
     *
     * @param method the PsiMethod
     */
    protected abstract void visitMethodMetric(PsiMethod method);

    /**
     * Visit field.
     *
     * @param field the PsiField
     */
    protected abstract void visitFieldMetric(PsiField field);

    /**
     * Visit branch.
     *
     * @param expr the PsiConditionalExpression
     */
    protected abstract void visitBranchMetric(PsiConditionalExpression expr);

    /**
     * Visit assert statement.
     *
     * @param statement the PsiAssertStatement
     */
    protected abstract void visitAssertStatementMetric(PsiAssertStatement statement);

    /**
     * Visit block statement.
     *
     * @param statement the PsiBlockStatement
     */
    protected abstract void visitBlockStatementMetric(PsiBlockStatement statement);

    /**
     * Visit break statement.
     *
     * @param statement the PsiBreakStatement
     */
    protected abstract void visitBreakStatementMetric(PsiBreakStatement statement);

    /**
     * Visit yield statement.
     *
     * @param statement the PsiYieldStatement
     */
    protected abstract void visitYieldStatementMetric(PsiYieldStatement statement);

    /**
     * Visit continue statement.
     *
     * @param statement the PsiContinueStatement
     */
    protected abstract void visitContinueStatementMetric(PsiContinueStatement statement);

    /**
     * Visit declaration statement.
     *
     * @param statement the PsiDeclarationStatement
     */
    protected abstract void visitDeclarationStatementMetric(PsiDeclarationStatement statement);

    /**
     * Visit do while statement.
     *
     * @param statement the PsiDoWhileStatement
     */
    protected abstract void visitDoWhileStatementMetric(PsiDoWhileStatement statement);

    /**
     * Visit empty statement.
     *
     * @param statement the PsiEmptyStatement
     */
    protected abstract void visitEmptyStatementMetric(PsiEmptyStatement statement);

    /**
     * Visit expression list statement.
     *
     * @param statement the PsiExpressionListStatement
     */
    protected abstract void visitExpressionListStatementMetric(PsiExpressionListStatement statement);

    /**
     * Visit expression statement.
     *
     * @param statement the PsiExpressionStatement
     */
    protected abstract void visitExpressionStatementMetric(PsiExpressionStatement statement);

    /**
     * Visit for statement.
     *
     * @param statement the PsiForStatement
     */
    protected abstract void visitForStatementMetric(PsiForStatement statement);

    /**
     * Visit foreach statement.
     *
     * @param statement the PsiForeachStatement
     */
    protected abstract void visitForeachStatementMetric(PsiForeachStatement statement);

    /**
     * Visit if statement.
     *
     * @param statement the PsiIfStatement
     */
    protected abstract void visitIfStatementMetric(PsiIfStatement statement);

    /**
     * Visit import statement.
     *
     * @param statement the PsiImportStatement
     */
    protected abstract void visitImportStatementMetric(PsiImportStatement statement);

    /**
     * Visit import static statement.
     *
     * @param statement the PsiImportStaticStatement
     */
    protected abstract void visitImportStaticStatementMetric(PsiImportStaticStatement statement);

    /**
     * Visit labeled statement.
     *
     * @param statement the PsiLabeledStatement
     */
    protected abstract void visitLabeledStatementMetric(PsiLabeledStatement statement);

    /**
     * Visit package statement.
     *
     * @param statement the PsiPackageStatement
     */
    protected abstract void visitPackageStatementMetric(PsiPackageStatement statement);

    /**
     * Visit return statement.
     *
     * @param statement the PsiReturnStatement
     */
    protected abstract void visitReturnStatementMetric(PsiReturnStatement statement);

    /**
     * Visit statement.
     *
     * @param statement the PsiStatement
     */
    protected abstract void visitStatementMetric(PsiStatement statement);

    /**
     * Visit switch label statement.
     *
     * @param statement the PsiSwitchLabelStatement
     */
    protected abstract void visitSwitchLabelStatementMetric(PsiSwitchLabelStatement statement);

    /**
     * Visit switch labeled rule statement.
     *
     * @param statement the PsiSwitchLabeledRuleStatement
     */
    protected abstract void visitSwitchLabeledRuleStatementMetric(PsiSwitchLabeledRuleStatement statement);

    /**
     * Visit switch statement.
     *
     * @param statement the PsiSwitchStatement
     */
    protected abstract void visitSwitchStatementMetric(PsiSwitchStatement statement);

    /**
     * Visit synchronized statement.
     *
     * @param statement the PsiSynchronizedStatement
     */
    protected abstract void visitSynchronizedStatementMetric(PsiSynchronizedStatement statement);

    /**
     * Visit throw statement.
     *
     * @param statement the PsiThrowStatement
     */
    protected abstract void visitThrowStatementMetric(PsiThrowStatement statement);

    /**
     * Visit try statement.
     *
     * @param statement the PsiTryStatement
     */
    protected abstract void visitTryStatementMetric(PsiTryStatement statement);

    /**
     * Visit while statement.
     *
     * @param statement the PsiWhileStatement
     */
    protected abstract void visitWhileStatementMetric(PsiWhileStatement statement);

    /**
     * Visit module statement.
     *
     * @param statement the PsiStatement
     */
    protected abstract void visitModuleStatementMetric(PsiStatement statement);

    /**
     * Visit requires statement.
     *
     * @param statement the PsiRequiresStatement
     */
    protected abstract void visitRequiresStatementMetric(PsiRequiresStatement statement);

    /**
     * Visit package accessibility statement.
     *
     * @param statement the PsiPackageAccessibilityStatement
     */
    protected abstract void visitPackageAccessibilityStatementMetric(PsiPackageAccessibilityStatement statement);

    /**
     * Visit uses statement.
     *
     * @param statement the PsiUsesStatement
     */
    protected abstract void visitUsesStatementMetric(PsiUsesStatement statement);

    /**
     * Visit provides statement.
     *
     * @param statement the PsiProvidesStatement
     */
    protected abstract void visitProvidesStatementMetric(PsiProvidesStatement statement);

    /**
     * Visit local variable.
     *
     * @param variable the PsiLocalVariable
     */
    protected abstract void visitLocalVariableMetric(PsiLocalVariable variable);

    /**
     * Visit assignment expression.
     *
     * @param expr the PsiAssignmentExpression
     */
    protected abstract void visitAssignmentExpressionMetric(PsiAssignmentExpression expr);

    /**
     * Visit parenthesized expression.
     *
     * @param expr the PsiParenthesizedExpression
     */
    protected abstract void visitParenthesizedExpressionMetric(PsiParenthesizedExpression expr);

    /**
     * Visit polyadic expression.
     *
     * @param expr the PsiPolyadicExpression
     */
    protected abstract void visitPolyadicExpressionMetric(PsiPolyadicExpression expr);

    /**
     * Visit unary expression.
     *
     * @param expr the PsiUnaryExpression
     */
    protected abstract void visitUnaryExpressionMetric(PsiUnaryExpression expr);

    /**
     * Gets visit result.
     *
     * @return the visit result
     */
    protected double getVisitResult() {
        return visitResult;
    }

    /**
     * Sets visit result.
     *
     * @param result the result
     */
    protected void setVisitResult(double result) {
        visitResult = result;
    }

    /**
     * Visit PsiElement.
     *
     * @param psiElement the PsiElement
     * @return the result
     */
    protected double visit(PsiElement psiElement) {
        psiElement.accept(visitor);
        return getVisitResult();
    }

    /**
     * Visit Project.
     *
     * @param project the Project
     * @return the result
     */
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
        setVisitResult(0);

        if (psiElement != null) {
            setMetric(visit(psiElement));
        } else if (project != null) {
            setMetric(visit(project));
        }

        return get();
    }
}
