package com.example;

public class ForTestClass {
    int test;

    void singleFor() {
        for (test = 0; test < 10; test++) {
            System.out.println(test);
        }
    }

    void singleForEach() {
        String[] temp = {"aa", "bb", "cc"};

        for (String el : temp) {
            System.out.println(el);
        }
    }

    void nestedFor() {
        String[] temp = {"aa", "bb", "cc"};
        for (test = 0; test < 10; test++) {
            for (String el : temp) {
                System.out.println(el);
            }
        }
    }

}
