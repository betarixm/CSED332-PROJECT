package org.csed332.project.team2;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JButtonAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The plugin tool window.
 */
public class ProjectToolWindow {
    private JPanel projectToolWindowContent;
    private final JButton buttonCalcMetric;
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
        var project = getActiveProject();
        projectToolWindowContent = new JPanel();

        buttonCalcMetric = new JButton("Calc Metrics");
        projectToolWindowContent.add(buttonCalcMetric);

        this.width = _width;
        this.height = _height;

        ActionListener listener = e -> {
            {
                // calc Metrics
                MetricWindow window = MetricWindow.getInstance(width, height);
                window.setMetrics();
                window.openWindow();
            }
        };

        buttonCalcMetric.addActionListener(listener);
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
}
