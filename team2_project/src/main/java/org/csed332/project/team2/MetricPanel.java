package org.csed332.project.team2;

import org.csed332.project.team2.metrics.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;

import org.jdom.Content;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class MetricPanel {
    private Metric metric;
    private JPanel panel;

    private TitledBorder basicTitle;
    private TitledBorder warnTitle;
    private Label metricValue;
    private JFreeChart metricChart;

    private Metric.Type type;

    MetricPanel(Metric _metric, Metric.Type _type) {
        this.metric = _metric;
        this.type = _type;

        basicTitle = BorderFactory.createTitledBorder(this.type.toString());
        warnTitle = BorderFactory.createTitledBorder("\u26A0" + this.type.toString());
        warnTitle.setTitleColor(Color.YELLOW);

        /* making JPanel */
        panel = new JPanel();
        setBasicTitle();

        /* Displays the metric value */
        metricValue = new Label();
        //metricValue.setText("");
        panel.add(metricValue);
        panel.setToolTipText(MetricDescription.get(this.type));

        /* Displays the metric chart */
        //Will have to refactor this part when implementing charts
        /*metricChart = BarChart.getChart(80);
        ChartPanel chartPanel = new ChartPanel(metricChart);
        chartPanel.setSize(200, 200);
        panel.add(chartPanel, BorderLayout.CENTER);
        chartPanel.validate();*/
    }


    public void updateMetric() {
        //TODO : after fix the bug of Metric.get it will work well
        double value = metric.calculate();
        metricValue.setText(Double.toString(value));
        //Should we update the panel?
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
