package org.csed332.project.team2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

/**
 * This class represents a BarChart.
 * This uses JFreeChart class to create the Chart.
 */
public class BarChart {

    // It could be changed to use given value from Backend
    // I just use Variable num, it could be replaced by other Values..

    /**
     * Constructor function for BarChart.
     * @param num for testing purpose. Will be changed to use values from the Backend.
     * @return the created BarChart.
     */
    public static JFreeChart getChart(int num) {
        return ChartFactory.createBarChart(
                "CODE-COVERAGE",
                "CATEGORY",
                "COVERAGE",
                createDataset(num),
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }

    /**
     * Create a dataset using the inputs.
     * Right now always return the same dataset. Will be changed.
     * @param num will be replaced by values from the Backend.
     * @return the created dataset.
     */
    static CategoryDataset createDataset(int num) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String b = "Branch";
        String c = "Condition";
        String m = "Method";

        dataset.addValue(num, m, "Tree");
        dataset.addValue(90, b, "Tree");
        dataset.addValue(90, c, "Tree");

        dataset.addValue(100, m, "Graph");
        dataset.addValue(40, b, "Graph");
        dataset.addValue(40, c, "Graph");

        dataset.addValue(70, m, "MutableGraph");
        dataset.addValue(20, b, "MutableGraph");
        dataset.addValue(60, c, "MutableGraph");


        return dataset;
    }

}
