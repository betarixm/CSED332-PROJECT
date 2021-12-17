package org.csed332.project.team2.metrics.halstead;

import org.eclipse.jdt.core.dom.*;

import java.util.HashMap;

public class HalsteadVisitor extends ASTVisitor {
    private HashMap<String, Integer> operands = new HashMap<>();
    private HashMap<String, Integer> operators = new HashMap<>();

    public int getNumberOfTotalOperators() {
        return operators.values().stream().reduce(0, (a, b) -> a + b);
    }

    public int getNumberOfUniqueOperators() {
        return operators.size();
    }

    public int getNumberOfTotalOperands() {
        return operands.values().stream().reduce(0, (a, b) -> a + b);
    }

    public int getNumberOfUniqueOperands() {
        return operands.size();
    }

    public boolean visit(CompilationUnit unit) {
        return true;
    }

    public boolean visit(ForStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node) {
        addOperator(node.getOperator().toString());
        return true;
    }

    @Override
    public boolean visit(PrefixExpression node) {
        addOperator(node.getOperator().toString());
        return true;
    }

    @Override
    public boolean visit(PostfixExpression node) {
        addOperator(node.getOperator().toString());
        return true;
    }

    @Override
    public boolean visit(Assignment node) {
        addOperator(node.getOperator().toString());
        return true;
    }

    public boolean visit(VariableDeclaration node) {
        addOperator("=");
        return true;
    }

    @Override
    public boolean visit(SimpleName name) {
        addOperand(name.getIdentifier());
        return true;
    }

    @Override
    public boolean visit(NullLiteral literal) {
        addOperand("null");
        return true;
    }

    @Override
    public boolean visit(StringLiteral literal) {
        addOperand(literal.getLiteralValue());
        return true;
    }

    @Override
    public boolean visit(CharacterLiteral literal) {
        addOperand(Character.toString(literal.charValue()));
        return true;
    }

    @Override
    public boolean visit(BooleanLiteral literal) {
        addOperand(Boolean.toString(literal.booleanValue()));
        return true;
    }

    @Override
    public boolean visit(NumberLiteral literal) {
        addOperand(literal.getToken());
        return true;
    }

    @Override
    public boolean visit(VariableDeclarationFragment fragment) {
        addOperator("=");
        return true;
    }

    public void addOperator(String operator) {
        if (!operators.containsKey(operator))
            operators.put(operator, 0);
        operators.put(operator, operators.get(operator) + 1);
    }

    public void addOperand(String operand) {
        if (!operands.containsKey(operand))
            operands.put(operand, 0);
        operands.put(operand, operands.get(operand) + 1);
    }
}
