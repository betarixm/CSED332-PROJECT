package org.csed332.project.team2.metrics;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;

public class ClassCodeLineMetric extends CodeLineMetric {

    public String className;

    public ClassCodeLineMetric(String path) {
        super(path);
        String namePlusJava = new File(path).getName();

        try {
            if (!namePlusJava.endsWith(".java"))
                throw new IllegalArgumentException("not java file!");
            this.className = namePlusJava.substring(0, namePlusJava.length() - 5);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        super.set(-1);
    }

    @Override
    public double get() {
        List<MetricModel> metricModelList = MetricModelService.getMetrics(getID(), className, 1);

        return metricModelList.isEmpty() ? super.get() : metricModelList.get(0).getFigure();
    }

    @Override
    protected void set(int value) {
        super.set(value);
        MetricModelService.saveMetric(getID(), className, value);
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