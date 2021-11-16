package org.csed332.project.team2;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The plugin tool window.
 */
public class ProjectToolWindow {
    private JPanel projectToolWindowContent;
    private final JButton buttonCalcMetric;
    private final JButton buttonWarning;
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
        projectToolWindowContent.setLayout(new BoxLayout(projectToolWindowContent, BoxLayout.PAGE_AXIS));

        buttonCalcMetric = new JButton("Calc Metrics");
        projectToolWindowContent.add(buttonCalcMetric);

        // TODO: this action then needs to be triggered from backend without a button
        buttonWarning = new JButton("Pls warn me");
        projectToolWindowContent.add(buttonWarning);

        MetricWindow window = MetricWindow.getInstance(width, height);
        projectToolWindowContent.add(window.getMetricContainer());

        this.width = _width;
        this.height = _height;

        ActionListener listener = e -> {
            {
                // calc Metrics TODO: this actually needs to update metrics, not add charts all the time
                window.setMetrics();
            }
        };

        buttonCalcMetric.addActionListener(listener);

        JLabel label = new JLabel("THIS IS A WARNING!");
        JPanel panel = new JPanel();
        panel.add(label);
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, projectToolWindowContent);
        ActionListener listenerWarning = e -> {
            {
                JBPopup popup = popupBuilder.createPopup();
                popup.showInFocusCenter();

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
}
