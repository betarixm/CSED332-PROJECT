package org.csed332.project.team2;

import com.google.api.Metric;
import com.google.zxing.oned.OneDimensionalCodeWriter;
import com.intellij.coverage.CoverageEditorAnnotator;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.sun.jna.platform.win32.COM.IEnumIDList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MetricWindow {
    // Single-tone pattern
    static private MetricWindow instance;
    final private JFrame window;
    final private JPanel[] metricPanel;

    enum Metric {HALSTED, CYCLO, INDEX, COVERAGE}

    public MetricWindow(int width, int height) {
        window = new JFrame();
        window.setSize(width, height);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        metricPanel = new JPanel[Metric.values().length];

        // Make window scrollable
        JScrollPane scrollPane = new JBScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        window.getContentPane().add(scrollPane);

        for (Metric metric : Metric.values()) {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder(metric.toString()));
            metricPanel[metric.ordinal()] = panel;
            container.add(panel);
        }
    }

    public static MetricWindow getInstance(int width, int height) {
        if (instance == null) {
            instance = new MetricWindow(width, height);
        }
        return instance;
    }

    public void setMetrics() {
        //TODO:calc metrics here? or get some data by argument?

        // example of code coverage
        JFreeChart coverageChart = BarChart.getChart(80);
        JPanel coveragePanel = metricPanel[Metric.COVERAGE.ordinal()];
        ChartPanel chartPanel = new ChartPanel(coverageChart);
        chartPanel.setSize(200, 200);
        coveragePanel.add(chartPanel, BorderLayout.CENTER);
        chartPanel.validate();


        //Example of Index code coverage
        JFreeChart coverageChart2 = BarChart.getChart(80);
        JPanel coveragePanel2 = metricPanel[Metric.INDEX.ordinal()];
        ChartPanel chartPanel2 = new ChartPanel(coverageChart2);
        chartPanel2.setSize(200, 200);
        coveragePanel2.add(chartPanel2, BorderLayout.CENTER);
        chartPanel2.validate();
    }

    public void openWindow() {
        window.setVisible(true);
    }

}
