package org.csed332.project.team2.metrics;

import java.io.IOException;
import java.io.FileReader;

public class ClassCodeLineMetric extends CodeLineMetric {

    public ClassCodeLineMetric(String path) {
        super(path);
    }

    @Override
    public double calculate() {
        try {
            int lines = 1;
            int readByte;
            FileReader fileReader = new FileReader(this.path);

            while ((readByte = fileReader.read()) != -1) {
                if ((char) readByte == '\n') lines++;
            }

            fileReader.close();
            set(lines);

            return lines;
        } catch (IOException e) {
            return -1;
        }
    }
}