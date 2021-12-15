package org.csed332.project.team2.metrics.halstead;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.csed332.project.team2.WarningCondition;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.VisitingMetric;

import java.util.HashMap;
import java.util.Map;

public class HalsteadMetric extends VisitingMetric {
    HalsteadType type;
    private static final Map<HalsteadType, Double> thresholds = Map.of(
            HalsteadType.DIFFICULTY,0.25,
            HalsteadType.EFFORT,100.0,
            HalsteadType.VOLUME,200.0
    );

    private static final double effortThreshold = 100;
    private static final double volumThreshold = 200;
    private static final double difficultyThreshold = 0.25;

    public HalsteadMetric(PsiElement element, HalsteadType type) {
        super(element);
        setID(Type.HALSTEAD.toString());
        this.type = type;

        if(thresholds.containsKey(type)) {
            Double threshold = thresholds.get(type);
            setCondition(new WarningCondition(WarningCondition.Mode.MORE_THAN, threshold));
        }

    }

    public HalsteadMetric(Project project, HalsteadType type) {
        super(project);
        setID(Type.HALSTEAD.toString());
        this.type = type;

        if(thresholds.containsKey(type)) {
            Double threshold = thresholds.get(type);
            setCondition(new WarningCondition(WarningCondition.Mode.MORE_THAN, threshold));
        }
    }

    public String getType() {
        return type.toString();
    }

    @Override
    public void save(CalcHistoryModel calc) {
        Map<PsiClass, Map<PsiMethod, Double>> metrics = getMetrics();
        for (PsiClass _class : metrics.keySet()) {
            for (PsiMethod _method : metrics.get(_class).keySet()) {
                Double _figure = metrics.get(_class).get(_method);
                MetricService.addMetric(getID(), _class.getName(), _method.getName(), type.toString(), _figure, calc);
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
        HalsteadParser halsteadParser = new HalsteadParser();
        halsteadParser.parse(method);
        HalsteadMetricCalculator calc = new HalsteadMetricCalculator(
                halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands(),
                halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperators(),
                halsteadParser.getHalsteadVisitor().getNumberOfTotalOperands(),
                halsteadParser.getHalsteadVisitor().getNumberOfUniqueOperands()
        );

        switch (type) {
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

    public enum HalsteadType {VOCABULARY, VOLUME, DIFFICULTY, EFFORT}
}
