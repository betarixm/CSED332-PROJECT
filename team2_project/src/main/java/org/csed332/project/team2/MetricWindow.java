package org.csed332.project.team2;

import com.intellij.openapi.project.Project;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;
import org.csed332.project.team2.metrics.ProjectCodeLineMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;

import javax.swing.*;

/**
 * The window displaying the metrics.
 */
public class MetricWindow {
    // Single-ton pattern
    static private MetricWindow instance;
    final private JPanel metricContainer;
    final private MetricPanel[] metricPanels;

    /**
     * Constructor for MetricWindow
     * The created window is scrollable and contains a panel with a title for each metric
     *
     * @param width  the width of the window
     * @param height the height of the window
     */
    public MetricWindow(int width, int height, Project project) {
        // We use a container to allow scrolling
        metricContainer = new JPanel();
        metricContainer.setLayout(new BoxLayout(metricContainer, BoxLayout.PAGE_AXIS));
        metricPanels = new MetricPanel[Metric.Type.values().length];

        // Add the metric panels to the window
        Metric codeLineMetric = new ProjectCodeLineMetric(project);
        MetricPanel codeLinePanel = new MetricPanel(new Metric[]{codeLineMetric}, Metric.Type.LINES_OF_CODE);
        metricPanels[0] = codeLinePanel;
        metricContainer.add(codeLinePanel.getPanel());

        BaseMetric[] halsteadMetrics = {
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.VOCABULARY),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.VOLUME),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.DIFFICULTY),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.EFFORT)
        };

        BaseMetricPanel halsteadPanel = new BaseMetricPanel(halsteadMetrics, Metric.Type.HALSTEAD, new String[]{"Vocabulary", "Volume", "Difficulty", "Effort"}, false);
        metricPanels[1] = halsteadPanel;
        metricContainer.add(halsteadPanel.getPanel());

        BaseMetric cycloMetric = new CyclomaticMetric(project);
        BaseMetricPanel cycloPanel = new BaseMetricPanel(new BaseMetric[]{cycloMetric}, Metric.Type.CYCLOMATIC, new String[]{"MetricValue"}, true);
        metricPanels[2] = cycloPanel;
        metricContainer.add(cycloPanel.getPanel());
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
    public static MetricWindow getInstance(int width, int height, Project project) {
        if (instance == null) {
            instance = new MetricWindow(width, height, project);
        }
        return instance;
    }

    public void setMetrics() {
        for (Metric.Type metric : Metric.Type.values()) {
            int idx = metric.ordinal();

            MetricPanel panel = metricPanels[idx];
            panel.updateMetric();
        }
    }

    public void showWarnMetric(Metric.Type[] warnMetrics) {
        for (Metric.Type metric : warnMetrics) {
            int idx = metric.ordinal();
            MetricPanel panel = metricPanels[idx];
            panel.setWarningTitle();
        }

        //TODO : show the how much the metric has worsen
    }
}
