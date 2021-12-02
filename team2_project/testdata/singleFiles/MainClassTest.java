package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainClassTest {
    @Test
    public void MainClassNotCrashingOnConstructor() {
        MainClassTest mainClass = new MainClassTest();
    }
}