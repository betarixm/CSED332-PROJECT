package org.csed332.project.team2.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.SlowOperations;
import com.intellij.util.ThrowableRunnable;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.Metric;
import org.csed332.project.team2.metrics.codeline.ProjectCodeLineMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * The plugin tool window.
 */
public class ProjectToolWindow {
    int width, height;
    Map<Metric.Type, Metric[]> metricList;
    private JPanel projectToolWindowContent;
    private JPanel toolbar;
    private JButton buttonCalcAndSave;

    /**
     * The ProjectToolWindow constructor.
     * Set up the window by:
     * - adding a button to calculate the metrics.
     *
     * @param toolWindow not used
     * @param _width     the width
     * @param _height    the height
     */
    public ProjectToolWindow(ToolWindow toolWindow, int _width, int _height) {
        this.width = _width;
        this.height = _height;
        this.metricList = new HashMap<>();

        var project = getActiveProject();
        projectToolWindowContent = new JPanel();
        projectToolWindowContent.setLayout(new BoxLayout(projectToolWindowContent, BoxLayout.PAGE_AXIS));
        createToolbar();

        projectToolWindowContent.add(toolbar);
        projectToolWindowContent.add(new JSeparator());


        Metric codeLineMetric = new ProjectCodeLineMetric(project);
        BaseMetric[] halsteadMetrics = {
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.VOCABULARY),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.VOLUME),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.DIFFICULTY),
                new HalsteadMetric(project, HalsteadMetric.HalsteadType.EFFORT)
        };
        BaseMetric cycloMetric = new CyclomaticMetric(project);

        metricList.put(Metric.Type.LINES_OF_CODE, new Metric[]{codeLineMetric});
        metricList.put(Metric.Type.HALSTEAD, halsteadMetrics);
        metricList.put(Metric.Type.CYCLOMATIC, new BaseMetric[]{cycloMetric});

        MetricWindow window = MetricWindow.getInstance(width, height, metricList);
        projectToolWindowContent.add(window.getMetricContainer());

        JPanel warnPanel = getWarning();
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(warnPanel, projectToolWindowContent);

        ActionListener calculateAndSaveListener = e -> backgroundOperation( () -> {
            System.out.println("Button pressed");
            doMetricsCalculation(popupBuilder, window);
            doMetricsSave();
        });

        buttonCalcAndSave.addActionListener(calculateAndSaveListener);
    }

    /**
     * Used in MyToolWindowFactory to display this window in intellij
     *
     * @return this window JPanel
     */
    public JPanel getContent() {
        return projectToolWindowContent;
    }

    /**
     * This function returns the user's active project (or an arbitrary project if none is open)
     *
     * @return the active project
     */
    @NotNull
    private Project getActiveProject() {
        for (var project : ProjectManager.getInstance().getOpenProjects()) {
            var window = WindowManager.getInstance().suggestParentWindow(project);
            if (window != null && window.isActive())
                return project;
        }
        return ProjectManager.getInstance().getOpenProjects()[0];
    }

    /**
     * This function make the warning message panel with some icons
     *
     * @return warning popup
     */
    private JPanel getWarning() {
        JPanel warnPanel = new JPanel();
        JLabel warnMessage = new JLabel("WARNING : Quality of the Metrics has degraded.");

        URL warnImg = ProjectToolWindow.class.getClassLoader().getResource("exclamation-mark.png");
        ImageIcon warnIcon = new ImageIcon(warnImg);
        Image tempImg = warnIcon.getImage();

        int iconSize = warnMessage.getFont().getSize() * 2;
        Image warnImg2 = tempImg.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH);
        JLabel warnIconLabel = new JLabel(new ImageIcon(warnImg2));

        warnPanel.add(warnIconLabel);
        warnPanel.add(warnMessage);
        return warnPanel;
    }

    private void createToolbar() {
        toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));
        buttonCalcAndSave = new JButton("Calculate Metrics");
        toolbar.add(buttonCalcAndSave);
    }

    private void backgroundOperation(ThrowableRunnable<?> runnable) {
        ProgressManager.getInstance().run(new Task.Backgroundable(null, "Calculating metrics...") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runReadAction(() -> {
                    try {
                        SlowOperations.allowSlowOperations(runnable);
                    } catch (Throwable e) {}
                }));
            }
        });
    }


    private void doMetricsCalculation(ComponentPopupBuilder popupBuilder, MetricWindow window){
        ArrayList<Metric.Type> warnMetric = new ArrayList<Metric.Type>();

        for (Metric.Type metric : Metric.Type.values()) {
            boolean warning = false;

            Metric[] subMetrics = metricList.get(metric);
            for (Metric subMetric : subMetrics) {
                subMetric.calculate();
                warning = warning || subMetric.checkDegradation();
            }
            if (warning) {
                warnMetric.add(metric);
            }
        }

        if (!warnMetric.isEmpty()) {
            JBPopup popup = popupBuilder.createPopup();
            popup.showInFocusCenter();
            System.out.println(warnMetric.get(0));
        }
        window.setMetrics(warnMetric);
    }

    private void doMetricsSave(){
        for (Metric.Type metric : Metric.Type.values()) {
            Metric[] subMetrics = metricList.get(metric);
            CalcHistoryModel calcHistoryModel = MetricService.generateCalcHistoryModel(subMetrics[0].getID());
            for (Metric subMetric : subMetrics) {
                if (subMetric instanceof BaseMetric) {
                    ((BaseMetric) subMetric).save(calcHistoryModel);
                }
            }
        }
    }

}
