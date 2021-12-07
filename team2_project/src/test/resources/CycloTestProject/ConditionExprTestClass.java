package com.example;

public class ConditionExprTestClass {
    int test;

    void declareExpr() {
        int a = test == 3 ? 0 : 1;
    }

    int returnExpr() {
        return test > 3 ? 0 : 1;
    }

    int blockExpr() {
        {
            test = test == 3 ? 0 : 1;
        }
    }

    int nestedExpr() {
        test = test == 3 ? 0 : (test > 4 ? 1 : 2);
    }
}
