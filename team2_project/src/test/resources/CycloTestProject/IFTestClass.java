package com.example;

public class IFTestClass {
    int test;

    public void singleIF() {
        if (test == 3) {
            test = 4;
        }
    }

    public void singleIfElse() {
        if (test == 3) {
            test = 4;
        } else {
            test = 10;
        }
    }

    public void nestedIF() {
        if (test > 3) {
            if (test > 10) {
                if (test > 20)
                    return;
            }
        }
    }

    public void multiElseIf() {
        if (test > 3) {

        } else if (test > 0) {

        } else if (test > -3) {

        } else {
            return;
        }
    }


}
