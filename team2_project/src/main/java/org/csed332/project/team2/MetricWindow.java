package org.csed332.project.team2;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

/**
 * The window displaying the metrics.
 */
public class MetricWindow {
    // Single-tone pattern
    static private MetricWindow instance;
    //    final private JFrame window;
    final private JPanel metricContainer;
    final private JPanel[] metricPanel;

    final private TitledBorder[] basicTitle;
    final private TitledBorder[] warnTitle;

    // The types of metrics displayed on the window
    enum Metric {HALSTED, CYCLO, INDEX, COVERAGE}

    /**
     * Constructor for MetricWindow
     * The created window is
     * - Scrollable
     * - Contains a panel with a title for each metric
     *
     * @param width  the width of the window
     * @param height the height of the window
     */
    public MetricWindow(int width, int height) {
        // We use a container to allow scrolling
        metricContainer = new JPanel();
        metricContainer.setLayout(new BoxLayout(metricContainer, BoxLayout.PAGE_AXIS));
        metricPanel = new JPanel[Metric.values().length];

        basicTitle = new TitledBorder[Metric.values().length];
        warnTitle = new TitledBorder[Metric.values().length];

        // Add the metric panels to the window
        for (Metric metric : Metric.values()) {
            JPanel panel = new JPanel();
            int idx = metric.ordinal();

            TitledBorder warnTitleBorder = BorderFactory.createTitledBorder("\u26A0" + metric.toString());
            warnTitleBorder.setTitleColor(Color.YELLOW);
            warnTitle[idx] = warnTitleBorder;
            basicTitle[idx] = BorderFactory.createTitledBorder(metric.toString());

            panel.setBorder(basicTitle[idx]);
            metricPanel[metric.ordinal()] = panel;
            metricContainer.add(panel);
        }
    }

    public JPanel getMetricContainer() {
        return metricContainer;
    }

    /**
     * Get the single-toned instance of this class.
     *
     * @param width  the width of the instance
     * @param height the height of the instance
     * @return the single-toned instance
     */
    public static MetricWindow getInstance(int width, int height) {
        if (instance == null) {
            instance = new MetricWindow(width, height);
        }
        return instance;
    }

    /**
     * Calculate the metric values by calling backend functions (will be done in latter updates)
     * Display them in the metric panels.
     */
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

    /**
     * change the title to warning title
     * (include Warning unicode and change the color to yellow)
     *
     * @param warnMetrics The Metrics with degrading quality
     */
    public void showWarnMetric(Metric[] warnMetrics) {

        for (Metric metric : warnMetrics) {
            int idx = metric.ordinal();

            JPanel panel = metricPanel[idx];
            panel.setBorder(warnTitle[idx]);
        }


        //TODO : show the how much the metric has worsen


    }
}
