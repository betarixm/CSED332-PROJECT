package com.example;

public class TryTestClass {
    public void singleTry() {

        try {

        } catch (Exception e) {

        }
    }

    public void nestedTry() {
        int test;

        try {

        } catch (Exception e) {
            try {
                test = 3;
            } catch (Exception e2) {
                return;
            }
        }
    }
}
