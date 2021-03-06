package org.csed332.project.team2.metrics.cyclomatic;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiEmptyExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiEmptyStatementImpl;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.VisitingMetric;
import org.csed332.project.team2.utils.WarningCondition;

import java.util.Map;
import java.util.Objects;

/**
 * Class for Cyclomatic metric.
 */
public class CyclomaticMetric extends VisitingMetric {
    /**
     * Instantiates a new CyclomaticMetric.
     *
     * @param element the element
     */
    public CyclomaticMetric(PsiElement element) {
        super(element);
        setID(Type.CYCLOMATIC.toString());
    }

    /**
     * Instantiates a new CyclomaticMetric.
     *
     * @param project the project
     */
    public CyclomaticMetric(Project project) {
        super(project);
        setID(Type.CYCLOMATIC.toString());
        setCondition(new WarningCondition(WarningCondition.Mode.MORE_THAN, 15));
    }

    @Override
    public void save(CalcHistoryModel calc) {
        Map<String, Map<PsiMethod, Double>> metrics = getMetrics();
        for (String _class : metrics.keySet()) {
            for (PsiMethod _method : metrics.get(_class).keySet()) {
                Double _figure = metrics.get(_class).get(_method);
                MetricService.addMetric(getID(), _class, _method.getName(), "", _figure, calc);
            }
        }
    }

    private PsiElement requireNonNullElse(PsiElement element) {
        return Objects.requireNonNullElse(element, new PsiEmptyStatementImpl());
    }

    @Override
    protected void visitPackageMetric(PsiPackage pack) {
        for (PsiPackage subPack : pack.getSubPackages()) {
            subPack.accept(visitor);
        }

        for (PsiClass aClass : pack.getClasses()) {
            aClass.accept(visitor);
        }
    }

    @Override
    protected void visitClassMetric(PsiClass aClass) {
        for (PsiMethod method : aClass.getMethods()) {
            double value = getVisitResult();
            method.accept(visitor);
            setVisitResult(getVisitResult() + 1);
            setMetric(getVisitResult() - value, aClass, method);
        }
    }

    @Override
    protected void visitMethodMetric(PsiMethod method) {
        PsiCodeBlock codeBlock = method.getBody();

        if (codeBlock != null) {
            for (PsiStatement statement : codeBlock.getStatements()) {
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
        requireNonNullElse(expr.getThenExpression()).accept(visitor);
        requireNonNullElse(expr.getElseExpression()).accept(visitor);
    }

    @Override
    protected void visitSwitchLabelStatementMetric(PsiSwitchLabelStatement sLabel) {
        if (!sLabel.isDefaultCase()) {
            setVisitResult(getVisitResult() + 1);
        }
    }

    @Override
    protected void visitAssertStatementMetric(PsiAssertStatement statement) {
        setVisitResult(getVisitResult() + 1);
        requireNonNullElse(statement.getAssertCondition()).accept(visitor);
        requireNonNullElse(statement.getAssertDescription()).accept(visitor);
    }

    @Override
    protected void visitBlockStatementMetric(PsiBlockStatement statement) {

        for (PsiStatement expr : statement.getCodeBlock().getStatements()) {
            expr.accept(visitor);
        }

    }

    @Override
    protected void visitBreakStatementMetric(PsiBreakStatement statement) {
    }

    @Override
    protected void visitYieldStatementMetric(PsiYieldStatement statement) {

    }

    @Override
    protected void visitContinueStatementMetric(PsiContinueStatement statement) {
    }

    @Override
    protected void visitDeclarationStatementMetric(PsiDeclarationStatement statement) {
        for (PsiElement element : statement.getDeclaredElements()) {
            element.accept(visitor);
        }
    }

    @Override
    protected void visitDoWhileStatementMetric(PsiDoWhileStatement statement) {
        setVisitResult(getVisitResult() + 1);

        requireNonNullElse(statement.getBody()).accept(visitor);
        requireNonNullElse(statement.getCondition()).accept(visitor);
    }

    @Override
    protected void visitEmptyStatementMetric(PsiEmptyStatement statement) {

    }

    @Override
    protected void visitExpressionListStatementMetric(PsiExpressionListStatement statement) {
        for (PsiExpression expr : statement.getExpressionList().getExpressions()) {
            expr.accept(visitor);
        }
    }

    @Override
    protected void visitExpressionStatementMetric(PsiExpressionStatement statement) {
        statement.getExpression().accept(visitor);
    }

    @Override
    protected void visitForStatementMetric(PsiForStatement statement) {
        setVisitResult(getVisitResult() + 1);

        requireNonNullElse(statement.getCondition()).accept(visitor);
        requireNonNullElse(statement.getBody()).accept(visitor);
    }

    @Override
    protected void visitForeachStatementMetric(PsiForeachStatement statement) {
        setVisitResult(getVisitResult() + 1);

        requireNonNullElse(statement.getIteratedValue()).accept(visitor);
        requireNonNullElse(statement.getBody()).accept(visitor);
    }

    @Override
    protected void visitIfStatementMetric(PsiIfStatement statement) {
        setVisitResult(getVisitResult() + 1);

        requireNonNullElse(statement.getCondition()).accept(visitor);
        requireNonNullElse(statement.getThenBranch()).accept(visitor);
        requireNonNullElse(statement.getElseBranch()).accept(visitor);
    }

    @Override
    protected void visitImportStatementMetric(PsiImportStatement statement) {

    }

    @Override
    protected void visitImportStaticStatementMetric(PsiImportStaticStatement statement) {

    }

    @Override
    protected void visitLabeledStatementMetric(PsiLabeledStatement statement) {
        requireNonNullElse(statement.getStatement()).accept(visitor);
    }

    @Override
    protected void visitPackageStatementMetric(PsiPackageStatement statement) {

    }

    @Override
    protected void visitReturnStatementMetric(PsiReturnStatement statement) {
        requireNonNullElse(statement.getReturnValue()).accept(visitor);
    }

    @Override
    protected void visitStatementMetric(PsiStatement statement) {

    }

    @Override
    protected void visitSwitchLabeledRuleStatementMetric(PsiSwitchLabeledRuleStatement statement) {
        requireNonNullElse(statement.getBody()).accept(visitor);
    }

    @Override
    protected void visitSwitchStatementMetric(PsiSwitchStatement statement) {
        requireNonNullElse(statement.getExpression()).accept(visitor);

        PsiCodeBlock codeBlock = statement.getBody();
        if (codeBlock != null) {
            for (PsiStatement expr : codeBlock.getStatements()) {
                expr.accept(visitor);
            }
        }

    }

    @Override
    protected void visitSynchronizedStatementMetric(PsiSynchronizedStatement statement) {
        requireNonNullElse(statement.getBody()).accept(visitor);

        PsiCodeBlock codeBlock = statement.getBody();
        if (codeBlock != null) {
            for (PsiStatement expr : codeBlock.getStatements()) {
                expr.accept(visitor);
            }
        }
    }

    @Override
    protected void visitThrowStatementMetric(PsiThrowStatement statement) {
        requireNonNullElse(statement.getException()).accept(visitor);
    }

    @Override
    protected void visitTryStatementMetric(PsiTryStatement statement) {
        setVisitResult(getVisitResult() + 1);

        PsiCodeBlock tryBlock = statement.getTryBlock();
        PsiCodeBlock finalBlock = statement.getFinallyBlock();

        if (tryBlock != null) {
            for (PsiStatement expr : tryBlock.getStatements()) {
                expr.accept(visitor);
            }
        }


        for (PsiCodeBlock block : statement.getCatchBlocks()) {
            for (PsiStatement expr : block.getStatements()) {
                expr.accept(visitor);
            }
        }


        if (finalBlock != null) {
            for (PsiStatement expr : finalBlock.getStatements()) {
                expr.accept(visitor);
            }
        }

    }

    @Override
    protected void visitWhileStatementMetric(PsiWhileStatement statement) {
        setVisitResult(getVisitResult() + 1);

        requireNonNullElse(statement.getCondition()).accept(visitor);
        requireNonNullElse(statement.getBody()).accept(visitor);
    }

    @Override
    protected void visitModuleStatementMetric(PsiStatement statement) {

    }

    @Override
    protected void visitRequiresStatementMetric(PsiRequiresStatement statement) {

    }

    @Override
    protected void visitPackageAccessibilityStatementMetric(PsiPackageAccessibilityStatement statement) {

    }

    @Override
    protected void visitUsesStatementMetric(PsiUsesStatement statement) {

    }

    @Override
    protected void visitProvidesStatementMetric(PsiProvidesStatement statement) {

    }

    @Override
    protected void visitLocalVariableMetric(PsiLocalVariable variable) {
        Objects.requireNonNullElse(variable.getInitializer(), new PsiEmptyExpressionImpl()).accept(visitor);
    }

    @Override
    protected void visitAssignmentExpressionMetric(PsiAssignmentExpression expr) {
        Objects.requireNonNullElse(expr.getLExpression(), new PsiEmptyExpressionImpl()).accept(visitor);
        Objects.requireNonNullElse(expr.getRExpression(), new PsiEmptyExpressionImpl()).accept(visitor);
    }

    @Override
    protected void visitParenthesizedExpressionMetric(PsiParenthesizedExpression expr) {
        Objects.requireNonNullElse(expr.getExpression(), new PsiEmptyExpressionImpl()).accept(visitor);
    }

    @Override
    protected void visitPolyadicExpressionMetric(PsiPolyadicExpression expr) {
        for (PsiExpression expression : expr.getOperands()) {
            expression.accept(visitor);
        }
    }

    @Override
    protected void visitUnaryExpressionMetric(PsiUnaryExpression expr) {
        Objects.requireNonNullElse(expr.getOperand(), new PsiEmptyExpressionImpl()).accept(visitor);
    }
}
