package org.csed332.project.team2.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var projectToolWindow = new ProjectToolWindow(toolWindow, 800, 800);
        var contentFactory = ContentFactory.SERVICE.getInstance();
        var content = contentFactory.createContent(projectToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
