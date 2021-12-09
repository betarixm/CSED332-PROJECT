package org.csed332.project.team2.metrics.halstead;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.csed332.project.team2.metrics.VisitingMetric;

import java.util.ArrayList;
import java.util.List;

public class HalsteadMetric extends VisitingMetric {
    public enum Type{ VOCABULARY, VOLUME, DIFFICULTY, EFFORT }
    Type type;

    public HalsteadMetric(PsiElement element, Type type) {
        super(element);
        setID(type.toString());
        this.type = type;
    }

    public HalsteadMetric(Project project, Type type) {
        super(project);
        setID(type.toString());
        this.type = type;
    }

    @Override
    public boolean checkDegradation() {
        //TODO: make this method return true if value of Halstead Metric degraded.
        return false;
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
        HalsteadParser halsteadParser = new HalsteadParser();
        halsteadParser.parse(method);
        HalsteadMetricCalculator calc = new HalsteadMetricCalculator(
                halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands(),
                halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperators(),
                halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands(),
                halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperands()
        );

        switch (type){
            case VOCABULARY:
                setVisitResult(getVisitResult() + calc.getVocabulary());
                break;

            case VOLUME:
                setVisitResult(getVisitResult() + calc.getVolume());
                break;

            case DIFFICULTY:
                setVisitResult(getVisitResult() + calc.getDifficulty());
                break;

            case EFFORT:
                setVisitResult(getVisitResult() + calc.getEfforts());
                break;
        }
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
