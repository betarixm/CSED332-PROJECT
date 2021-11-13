package org.csed332.project.team2;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.sun.jna.platform.win32.WinBase;
import org.csed332.project.team2.metrics.FileCoverageMetric;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProjectToolWindow {
    private JPanel projectToolWindowContent;
    public FileCoverageMetric fileCoverageMetric;

    public ProjectToolWindow(ToolWindow toolWindow) {
        var project = getActiveProject();
        projectToolWindowContent = new JPanel();
        fileCoverageMetric = new FileCoverageMetric(System.out);
        try {
            System.out.println("read: " + project.getBasePath());
            fileCoverageMetric.execute(new String[]{project.getBasePath()});
            // TODO: add a new way to detect testing directories automatically
            // fileCoverageMetric.runTest(project.getBasePath() + "/src/test/...");
        } catch(Throwable e) {
            System.out.println(e.getMessage());
        }
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
