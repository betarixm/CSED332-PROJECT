package org.csed332.project.team2;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetricCalculator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseMetricPanel extends MetricPanel {
    List<BaseMetric> baseMetrics;
    JBTable table;
    DefaultTableModel tableModel;
    private boolean computeTotal = false;
    private List<String> columNames;

    public BaseMetricPanel(BaseMetric[] _metrics, Metric.Type _type, String[] _columnNames, boolean _computeTotal) {
        super(_metrics, _type);
        baseMetrics = List.of(_metrics);
        columNames = List.of(_columnNames);
        computeTotal = _computeTotal;
        setTableModel();

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
        List<Pair<String, Double>> totalMetrics = new ArrayList<>();
        List<Pair<String, Map<PsiClass, Map<PsiMethod, Double>>>> values = new ArrayList<>();

        for (BaseMetric baseMetric : baseMetrics) {
            totalMetrics.add(Pair.create(baseMetric.getID(), baseMetric.calculate()));
            values.add(Pair.create(baseMetric.getID(), (baseMetric).getMetrics()));
        }

        setTableModel();
        table.setModel(tableModel);
        Map<List<String>, List<Double>> tableRowMap = new HashMap<>();

        for (Pair<String, Map<PsiClass, Map<PsiMethod, Double>>> value : values) {
            for (Map.Entry<PsiClass, Map<PsiMethod, Double>> entry : value.second.entrySet()) {
                String aClass = entry.getKey().getName();
                for (Map.Entry<PsiMethod, Double> subEntry : entry.getValue().entrySet()) {
                    Double aValue = subEntry.getValue();
                    String aMethod = subEntry.getKey().getName();
                    List<String> listKey = new ArrayList<>(List.of(new String[]{aClass, aMethod}));
                    List<Double> valueList = tableRowMap.getOrDefault(listKey, new ArrayList<>());
                    valueList.add(aValue);
                    tableRowMap.put(listKey, valueList);
                }
            }
        }

        for (List<String> key : tableRowMap.keySet()) {
            String aClass = key.get(0);
            String aMethod = key.get(1);
            List<Double> listValues = tableRowMap.get(key);
            Object[] rowData = new Object[2 + listValues.size()];
            rowData[0] = aClass;
            rowData[1] = aMethod;
            for (int i = 0; i < listValues.size(); i++) {
                rowData[i+2] = listValues.get(i).toString();
            }
            tableModel.addRow(rowData);
        }

        if (computeTotal) {
            double totalMetric = baseMetrics.get(0).calculate();
            metricValues.get(0).setText("Total : " + Double.toString(totalMetric));
        }
    }

    public void setTableModel() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Class");
        tableModel.addColumn("Method");
        for (String valueName : columNames) {
            tableModel.addColumn(valueName);
        }
    }
}
