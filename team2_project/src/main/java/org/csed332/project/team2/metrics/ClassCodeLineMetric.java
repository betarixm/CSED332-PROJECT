package org.csed332.project.team2.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class ClassCodeLineMetric extends CodeLineMetric {

    public ClassCodeLineMetric(String path) {
        super(path);
    }

    @Override
    public double calculate() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(this.path))) {
            int lines = 0;

            while (fileReader.readLine() != null)
                lines++;

            set(lines);

            return lines;
        } catch (IOException e) {
            return -1;
        }
    }
}