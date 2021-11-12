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

public class ProjectToolWindow {
    private JPanel projectToolWindowContent;
    private final JButton buttonCalcMetric;


    public ProjectToolWindow(ToolWindow toolWindow) {
        var project = getActiveProject();
        projectToolWindowContent = new JPanel();

        buttonCalcMetric = new JButton("Calc Metrics");
        projectToolWindowContent.add(buttonCalcMetric);

        ActionListener listner = e -> {
            {
                // calc Metrics
                MetricWindow window = MetricWindow.getInstance(800, 800);
                window.setMetrics();
                window.openWindow();
            }
        };

       buttonCalcMetric.addActionListener(listner);
    }

    public JPanel getContent() {
        return projectToolWindowContent;
    }

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
