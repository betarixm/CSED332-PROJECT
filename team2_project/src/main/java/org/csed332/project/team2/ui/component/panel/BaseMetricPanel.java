package org.csed332.project.team2.ui.component.panel;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJvmMember;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

/**
 * Panel displaying metrics implemented with BaseMetric.
 */
public class BaseMetricPanel extends MetricPanel {
    List<BaseMetric> baseMetrics;
    JBTable table;
    DefaultTableModel tableModel;
    private boolean computeTotal = false;
    private List<String> columNames;

    private List<PsiMethod> methodList;
    private List<Set<PsiMethod>> warnMethod;
    private NumberFormat metricValueFormatter = new DecimalFormat("#0.0");
    private Comparator<Pair<String, PsiMethod>> rowComparator = new Comparator<Pair<String, PsiMethod>>() {
        @Override
        public int compare(Pair<String, PsiMethod> o1, Pair<String, PsiMethod> o2) {
            return (o1.first + o1.second.getName())
                    .compareTo(o2.first + o2.second.getName());
        }
    };

    /**
     * Instantiates a new BaseMetricPanel.
     *
     * @param _metrics      the BaseMetrics
     * @param _type         the type
     * @param _columnNames  the column names
     * @param _computeTotal whether to compute total metric or not
     */
    public BaseMetricPanel(BaseMetric[] _metrics, Metric.Type _type, String[] _columnNames, boolean _computeTotal) {
        super(_metrics, _type);
        baseMetrics = List.of(_metrics);
        columNames = List.of(_columnNames);
        computeTotal = _computeTotal;
        methodList = new ArrayList<>();
        warnMethod = new ArrayList<>();

        setTableModel();
        table = new JBTable(tableModel);
        table.setShowColumns(true);
        table.setRowHeight(10);
        getPanel().add(table);

        setTableProperty();

        int width = resizeColumnWidth();

        JBScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(width, width));
        getPanel().add(scrollPane);
    }

    /**
     * Resize column width.
     *
     * @return the total width
     */
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
    public void updateMetric(boolean warn) {
        List<Pair<String, Double>> totalMetrics = new ArrayList<>();
        List<Pair<String, Map<String, Map<PsiMethod, Double>>>> values = new ArrayList<>();

        methodList.clear();
        warnMethod.clear();

        if (warn) {
            setWarningTitle();
        } else {
            setBasicTitle();
        }

        for (BaseMetric baseMetric : baseMetrics) {
            totalMetrics.add(Pair.create(baseMetric.getID(), baseMetric.get()));
            values.add(Pair.create(baseMetric.getID(), (baseMetric).getMetrics()));
            Set<PsiMethod> subMethods = new HashSet<>();
            if (warn) {

                Map<String, Set<PsiMethod>> degradation = baseMetric.getDegradationMetrics();

                for (Set<PsiMethod> methods : degradation.values()) {
                    subMethods.addAll(methods);
                }
            }
            warnMethod.add(subMethods);
        }

        setTableModel();
        table.setModel(tableModel);
        SortedMap<Pair<String, PsiMethod>, List<Double>> tableRowMap = new TreeMap<Pair<String, PsiMethod>, List<Double>>(rowComparator);

        for (Pair<String, Map<String, Map<PsiMethod, Double>>> value : values) {
            for (Map.Entry<String, Map<PsiMethod, Double>> entry : value.second.entrySet()) {
                String aClass = entry.getKey();
                for (Map.Entry<PsiMethod, Double> subEntry : entry.getValue().entrySet()) {
                    Double aValue = subEntry.getValue();
                    PsiMethod aMethod = subEntry.getKey();
                    Pair<String, PsiMethod> listKey = new Pair(aClass, aMethod);
                    List<Double> valueList = tableRowMap.getOrDefault(listKey, new ArrayList<>());
                    valueList.add(aValue);
                    tableRowMap.put(listKey, valueList);
                }
            }
        }

        for (Pair<String, PsiMethod> key : tableRowMap.keySet()) {
            String aClass = key.first;
            PsiMethod aMethod = key.second;

            List<Double> listValues = tableRowMap.get(key);
            Object[] rowData = new Object[2 + listValues.size()];
            rowData[0] = aClass;
            rowData[1] = aMethod.getName();
            for (int i = 0; i < listValues.size(); i++) {
                rowData[i + 2] = metricValueFormatter.format(listValues.get(i));
            }
            tableModel.addRow(rowData);
            methodList.add(aMethod);
        }

        if (computeTotal) {
            double totalMetric = baseMetrics.get(0).get();
            metricValues.get(0).setText("Total : " + metricValueFormatter.format(totalMetric));
        }
    }

    /**
     * Sets table model.
     */
    public void setTableModel() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Class");
        tableModel.addColumn("Method");
        for (String valueName : columNames) {
            tableModel.addColumn(valueName);
        }
    }

    /**
     * Sets table property.
     */
    public void setTableProperty() {

        MouseListener tableListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (!methodList.isEmpty()) {
                    PsiJvmMember codePart = methodList.get(row);
                    codePart.navigate(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        table.addMouseListener(tableListener);

        DefaultTableCellRenderer metricRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                PsiMethod target = methodList.get(row);
                boolean setWarn = false;

                if (column >= 2) {
                    Set<PsiMethod> compareList = warnMethod.get(column - 2);
                    setWarn = compareList.contains(target);
                }

                if (setWarn) {
                    c.setForeground(Color.YELLOW);
                } else {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        table.setDefaultRenderer(table.getColumnClass(0), metricRenderer);
    }
}