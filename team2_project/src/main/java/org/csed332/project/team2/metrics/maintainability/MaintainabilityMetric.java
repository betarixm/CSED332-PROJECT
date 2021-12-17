package org.csed332.project.team2.metrics.maintainability;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.VisitingMetric;
import org.csed332.project.team2.metrics.codeline.CodeLineMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.csed332.project.team2.utils.WarningCondition;

import java.util.Map;

public class MaintainabilityMetric extends VisitingMetric {
    public MaintainabilityMetric(PsiElement element) {
        super(element);
        setID(Type.MAINTAINABILITY.toString());
    }

    public MaintainabilityMetric(Project project) {
        super(project);
        setID(Type.MAINTAINABILITY.toString());
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
            setMetric(getVisitResult() - value, aClass, method);
        }
    }

    @Override
    protected void visitMethodMetric(PsiMethod method) {
        HalsteadMetric halsteadMetric = new HalsteadMetric(method, HalsteadMetric.HalsteadType.VOLUME);
        CyclomaticMetric cyclomaticMetric = new CyclomaticMetric(method);
        int codeLine = method.getText().split("\n").length;

        double maintainabilityIndex = 171 - 5.2 * Math.log(halsteadMetric.calculate())
                - 0.23 * (cyclomaticMetric.calculate())
                - 16.2 * Math.log(codeLine) * 100 / 171;
        setVisitResult(Math.max(0, maintainabilityIndex));
    }

    @Override
    protected void visitFieldMetric(PsiField field) {

    }

    @Override
    protected void visitBranchMetric(PsiConditionalExpression expr) {

    }

    @Override
    protected void visitAssertStatementMetric(PsiAssertStatement statement) {

    }

    @Override
    protected void visitBlockStatementMetric(PsiBlockStatement statement) {

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

    }

    @Override
    protected void visitDoWhileStatementMetric(PsiDoWhileStatement statement) {

    }

    @Override
    protected void visitEmptyStatementMetric(PsiEmptyStatement statement) {

    }

    @Override
    protected void visitExpressionListStatementMetric(PsiExpressionListStatement statement) {

    }

    @Override
    protected void visitExpressionStatementMetric(PsiExpressionStatement statement) {

    }

    @Override
    protected void visitForStatementMetric(PsiForStatement statement) {

    }

    @Override
    protected void visitForeachStatementMetric(PsiForeachStatement statement) {

    }

    @Override
    protected void visitIfStatementMetric(PsiIfStatement statement) {

    }

    @Override
    protected void visitImportStatementMetric(PsiImportStatement statement) {

    }

    @Override
    protected void visitImportStaticStatementMetric(PsiImportStaticStatement statement) {

    }

    @Override
    protected void visitLabeledStatementMetric(PsiLabeledStatement statement) {

    }

    @Override
    protected void visitPackageStatementMetric(PsiPackageStatement statement) {

    }

    @Override
    protected void visitReturnStatementMetric(PsiReturnStatement statement) {

    }

    @Override
    protected void visitStatementMetric(PsiStatement statement) {

    }

    @Override
    protected void visitSwitchLabelStatementMetric(PsiSwitchLabelStatement statement) {

    }

    @Override
    protected void visitSwitchLabeledRuleStatementMetric(PsiSwitchLabeledRuleStatement statement) {

    }

    @Override
    protected void visitSwitchStatementMetric(PsiSwitchStatement statement) {

    }

    @Override
    protected void visitSynchronizedStatementMetric(PsiSynchronizedStatement statement) {

    }

    @Override
    protected void visitThrowStatementMetric(PsiThrowStatement statement) {

    }

    @Override
    protected void visitTryStatementMetric(PsiTryStatement statement) {

    }

    @Override
    protected void visitWhileStatementMetric(PsiWhileStatement statement) {

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

    }

    @Override
    protected void visitAssignmentExpressionMetric(PsiAssignmentExpression expr) {

    }

    @Override
    protected void visitParenthesizedExpressionMetric(PsiParenthesizedExpression expr) {

    }

    @Override
    protected void visitPolyadicExpressionMetric(PsiPolyadicExpression expr) {

    }

    @Override
    protected void visitUnaryExpressionMetric(PsiUnaryExpression expr) {

    }
}
