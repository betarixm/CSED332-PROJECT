package org.csed332.project.team2.metrics;

import java.io.File;

public class ProjectCodeLineMetric extends CompositeCodeLineMetric {
    public ProjectCodeLineMetric(String path) {
        super(path + "/src/main/java");
    }

    public ProjectCodeLineMetric() {
        this(new File("").getAbsolutePath() + "/src/main/java");
    }

    public ProjectCodeLineMetric(Project project) {
        this(project.getBasePath());
    }

    /**
     * Returns the open project of the current IntelliJ IDEA window
     *
     * @return the project
     */
    @NotNull
    private static Project getActiveProject() {
        for (var project : ProjectManager.getInstance().getOpenProjects()) {
            var window = WindowManager.getInstance().suggestParentWindow(project);
            if (window != null && window.isActive())
                return project;
        }
        // if there is no active project, return an arbitrary project (the first)
        return ProjectManager.getInstance().getOpenProjects()[0];
    }
}
