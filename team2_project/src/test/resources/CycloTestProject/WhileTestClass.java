package com.example;

public class MainClass {
    public void singleWhile() {
        int a;

        while (a != 3) {
            return;
        }
    }

    public void singleDoWhile() {
        int b;

        do {
            return;
        } while (b == 3);
    }

    public int nestedWhile() {
        int c;

        while (c > 3) {
            while (c > 10) {
                return c;
            }
        }

        do {
            do {
                return c;
            } while (c != 3);
        } while (c > 0);
    }

}
