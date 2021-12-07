package com.example;

public class SwitchTestClass {
    public void singleSwitch() {
        int test;

        switch (test) {

        }
    }

    public void defaultSwitch() {
        int test;

        switch (test) {
            default:
                return;
        }
    }

    public void multiCaseSwitch() {
        int test;

        switch (test) {
            case 1:
                return;
            case 2:
                test = 3;
            default:
                test = 10;
        }
    }

    public void nestedSwitch() {
        int test;

        switch (test) {
            case 1: {
                switch (test + 2) {
                    case 100:
                        return;
                }
            }
            default:
                test = 10;
        }
    }

}
