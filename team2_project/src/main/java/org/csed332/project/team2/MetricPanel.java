package org.csed332.project.team2;

import com.intellij.util.SlowOperations;
import org.csed332.project.team2.metrics.Metric;
import org.csed332.project.team2.utils.MetricDescription;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MetricPanel {
    // TODO: expand metric to metrics, metricValue to metricValues
    private List<Metric> metrics;
    private JPanel panel;

    private TitledBorder basicTitle;
    private TitledBorder warnTitle;
    protected List<Label> metricValues;
    private JFreeChart metricChart;

    private Metric.Type type;

    MetricPanel(Metric[] _metrics, Metric.Type _type) {
        this.metrics = List.of(_metrics);
        this.type = _type;

        basicTitle = BorderFactory.createTitledBorder(this.type.toString());
        warnTitle = BorderFactory.createTitledBorder("\u26A0" + this.type.toString());
        warnTitle.setTitleColor(Color.YELLOW);

        /* making JPanel */
        panel = new JPanel();
        setBasicTitle();

        /* Displays the metric value */
        metricValues = new ArrayList<>();
        metricValues.add(new Label());
        panel.add(metricValues.get(0));
        panel.setToolTipText(MetricDescription.get(this.type));
    }

    public void updateMetric(boolean warn) {
        for (int i = 0; i < metrics.size(); i++) {
            double value = metrics.get(i).calculate();
            metrics.get(i).save();

            if (warn) {
                setWarningTitle();
            } else {
                setBasicTitle();
            }

            metricValues.get(i).setText(Double.toString(value));
        }
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void setWarningTitle() {
        panel.setBorder(warnTitle);
    }

    public void setBasicTitle() {
        panel.setBorder(basicTitle);
    }

}
