package org.csed332.project.team2.metrics;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.table.JBTable;
import kotlin.random.Random;
import org.csed332.project.team2.MetricPanel;

import javax.swing.*;
import javax.swing.table.*;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTreeTable;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.awt.*;
import java.util.Map;

public class BaseMetricPanel extends MetricPanel {
    BaseMetric baseMetric;
    JBTable table;
    DefaultTableModel tableModel;

    public BaseMetricPanel(BaseMetric _metric, Metric.Type _type) {
        super(_metric, _type);
        baseMetric = _metric;
        tableModel = new DefaultTableModel();

        tableModel.addColumn("Class");
        tableModel.addColumn("Method");
        tableModel.addColumn("MetricValue");

        table = new JBTable(tableModel);
        table.setShowColumns(true);
        table.setRowHeight(10);

        getPanel().add(table);


        int width = resizeColumnWidth();

        JBScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(width, width));
        getPanel().add(scrollPane);


    }

    public int resizeColumnWidth() {
        final TableColumnModel columnModel = table.getColumnModel();
        int totalWidth = 0;

        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 100;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component component = table.prepareRenderer(renderer, row, column);
                width = Math.max(component.getPreferredSize().width, width);
            }
            width += 20;
            columnModel.getColumn(column).setPreferredWidth(width);
            columnModel.getColumn(column).setMaxWidth(width);
            totalWidth += width;
        }

        return totalWidth;
    }

    @Override
    public void updateMetric() {
        double totalMetric = baseMetric.calculate();
        Map<PsiClass, Map<PsiMethod, Double>> value = (baseMetric).getMetrics();

        for (Map.Entry<PsiClass, Map<PsiMethod, Double>> entry : value.entrySet()) {
            String aClass = entry.getKey().getName();

            for (Map.Entry<PsiMethod, Double> subEntry : entry.getValue().entrySet()) {
                Double aValue = subEntry.getValue();
                String aMethod = subEntry.getKey().getName();

                tableModel.addRow(new Object[]{aClass, aMethod, aValue.toString()});
            }
        }

        metricValue.setText("Total : " + Double.toString(totalMetric));
    }
}
