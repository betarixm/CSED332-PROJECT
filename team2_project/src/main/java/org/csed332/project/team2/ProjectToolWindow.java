package org.csed332.project.team2;

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
import org.csed332.project.team2.metrics.Metric;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * The plugin tool window.
 */
public class ProjectToolWindow {
    private JPanel projectToolWindowContent;
    private JPanel toolbar;
    private JButton buttonCalcMetric;
    private JButton buttonSaveMetric;
    private JButton buttonWarning;
    int width, height;

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

        var project = getActiveProject();
        projectToolWindowContent = new JPanel();
        projectToolWindowContent.setLayout(new BoxLayout(projectToolWindowContent, BoxLayout.PAGE_AXIS));
        createToolbar();

        // TODO: this action then needs to be triggered from backend without a button
        buttonWarning = new JButton("Show sample warning");
        toolbar.add(buttonWarning);
        projectToolWindowContent.add(toolbar);
        projectToolWindowContent.add(new JSeparator());

        MetricWindow window = MetricWindow.getInstance(width, height, project);
        projectToolWindowContent.add(window.getMetricContainer());

        ActionListener listener = e -> {
            {
                ProgressManager.getInstance().run(new Task.Backgroundable(null, "Calculating metrics...") {
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        ApplicationManager.getApplication().invokeLater(() -> {
                            ApplicationManager.getApplication().runReadAction(() -> {
                                SlowOperations.allowSlowOperations(() -> {
                                    window.setMetrics();
                                });
                            });
                        });
                    }
                });
            }
        };

        buttonCalcMetric.addActionListener(listener);
        JPanel warnPanel = getWarning();

        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(warnPanel, projectToolWindowContent);
        ActionListener listenerWarning = e -> {
            {
                JBPopup popup = popupBuilder.createPopup();
                popup.showInFocusCenter();

                // TODO what metrics makes degrading? it should be passed from backend
                Metric.Type[] warnMetric = {Metric.Type.LINES_OF_CODE};
                window.showWarnMetric(warnMetric);

            }
        };
        buttonWarning.addActionListener(listenerWarning);
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
        // if there is no active project, return an arbitrary project (the first)
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

        buttonCalcMetric = new JButton("Calculate Metrics");
        toolbar.add(buttonCalcMetric);

        buttonSaveMetric = new JButton("Save Metrics");
        toolbar.add(buttonSaveMetric);
    }
}
