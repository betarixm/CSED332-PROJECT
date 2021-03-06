package org.csed332.project.team2.ui;

import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;
import org.csed332.project.team2.ui.component.panel.BaseMetricPanel;
import org.csed332.project.team2.ui.component.panel.MetricPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * The window displaying the metrics.
 */
public class MetricWindow {
    static private MetricWindow instance;
    final private JPanel metricContainer;
    final private MetricPanel[] metricPanels;

    /**
     * Constructor for MetricWindow
     * The created window is scrollable and contains a panel with a title for each metric
     *
     * @param width      the width of the window
     * @param height     the height of the window
     * @param metricList the metric list
     */
    public MetricWindow(int width, int height, Map<Metric.Type, Metric[]> metricList) {
        metricContainer = new JPanel();
        metricContainer.setLayout(new BoxLayout(metricContainer, BoxLayout.PAGE_AXIS));
        metricPanels = new MetricPanel[Metric.Type.values().length];

        MetricPanel codeLinePanel = new MetricPanel(metricList.get(Metric.Type.LINES_OF_CODE), Metric.Type.LINES_OF_CODE);
        metricPanels[0] = codeLinePanel;
        metricContainer.add(codeLinePanel.getPanel());

        BaseMetricPanel halsteadPanel = new BaseMetricPanel((BaseMetric[]) metricList.get(Metric.Type.HALSTEAD), Metric.Type.HALSTEAD, new String[]{"Vocabulary", "Volume", "Difficulty", "Effort"}, false);
        metricPanels[1] = halsteadPanel;
        metricContainer.add(halsteadPanel.getPanel());

        BaseMetricPanel cycloPanel = new BaseMetricPanel((BaseMetric[]) metricList.get(Metric.Type.CYCLOMATIC), Metric.Type.CYCLOMATIC, new String[]{"MetricValue"}, true);
        metricPanels[2] = cycloPanel;
        metricContainer.add(cycloPanel.getPanel());
    }

    /**
     * Get the single-toned instance of this class.
     *
     * @param width      the width of the instance
     * @param height     the height of the instance
     * @param metricList the metric list
     * @return the single-toned instance
     */
    public static MetricWindow getInstance(int width, int height, Map<Metric.Type, Metric[]> metricList) {
        if (instance == null) {
            instance = new MetricWindow(width, height, metricList);
        }
        return instance;
    }

    /**
     * Gets metric container panel.
     *
     * @return the JPanel
     */
    public JPanel getMetricContainer() {
        return metricContainer;
    }

    /**
     * Runs updateMetric for all MetricPanel
     *
     * @param warnMetric the list of metric types that should be warned
     */
    public void setMetrics(ArrayList<Metric.Type> warnMetric) {
        for (Metric.Type metric : Metric.Type.values()) {
            int idx = metric.ordinal();
            boolean warn = warnMetric.contains(metric);

            MetricPanel panel = metricPanels[idx];
            panel.updateMetric(warn);
        }
    }

    /**
     * Set some metrics to warning state.
     *
     * @param warnMetrics the array of metric types to set as warning
     */
    public void showWarnMetric(Metric.Type[] warnMetrics) {
        for (Metric.Type metric : warnMetrics) {
            int idx = metric.ordinal();
            MetricPanel panel = metricPanels[idx];
            panel.setWarningTitle();
        }
    }
}
