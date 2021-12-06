package org.csed332.project.team2;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.*;

public class BaseMetricPanel extends MetricPanel {
    List<BaseMetric> baseMetrics;
    JBTable table;
    DefaultTableModel tableModel;

    public BaseMetricPanel(BaseMetric[] _metrics, Metric.Type _type) {
        super(_metrics, _type);
        baseMetrics = List.of(_metrics);
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
        Map<List<String>, Double> tableRowMap = new HashMap<>();

        for (Pair<String, Map<PsiClass, Map<PsiMethod, Double>>> value : values) {

            String anId = value.first;
            for (Map.Entry<PsiClass, Map<PsiMethod, Double>> entry : value.second.entrySet()) {
                String aClass = entry.getKey().getName();
                for (Map.Entry<PsiMethod, Double> subEntry : entry.getValue().entrySet()) {

                    Double aValue = subEntry.getValue();
                    String aMethod = subEntry.getKey().getName();

                    List<String> listKey = new ArrayList<>(List.of(new String[]{aClass, aMethod, anId}));
                    tableRowMap.put(listKey, aValue);

                    tableModel.addRow(new Object[]{aClass, aMethod, aValue.toString()});
                }
            }
        }
        List<List<String>> tableRowList = new ArrayList<>();
        for (List<String> key : tableRowMap.keySet()) {

            String aClass = key.get(0);
            String aMethod = key.get(1);
            String aType = key.get(2);

            String total_operators = "";
            String unique_operators = "";
            String total_operands = "";
            String unique_operands = "";

            switch (aType) {
                case ("TOTAL_OPERATORS"):
                    total_operators = tableRowMap.get(key).toString();
                    break;
                case ("UNIQUE_OPERATORS"):
                    unique_operators = tableRowMap.get(key).toString();
                    break;
                case ("TOTAL_OPERANDS"):
                    total_operands = tableRowMap.get(key).toString();
                    break;
                case ("UNIQUE_OPERANDS"):
                    unique_operands = tableRowMap.get(key).toString();
                    break;
            }
            tableRowList.add(List.of(new String[]{aClass, aMethod, total_operators, unique_operators, total_operands, unique_operands}));

        }

        for (List<String> list : tableRowList) {
            tableModel.addRow((Vector<?>) list);
        }
    }

    public void setTableModel() {
        tableModel = new DefaultTableModel();

        tableModel.addColumn("Class");
        tableModel.addColumn("Method");
        for (int i = 0; i < baseMetrics.size(); i++) {
            tableModel.addColumn("MetricValue" + i);
        }
    }
}
