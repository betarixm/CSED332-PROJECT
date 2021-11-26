package org.csed332.project.team2.metrics;

import com.intellij.ide.projectView.impl.nodes.PackageUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.JavaDirectoryService;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import java.util.*;

public class ProjectCodeLineMetric extends CompositeCodeLineMetric {

    public ProjectCodeLineMetric() {
        this(getActiveProject());
    }

    public ProjectCodeLineMetric(Project project) {
        Set<PsiPackage> psiPackages = getRootPackages(project);

        PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeAdapter() {
            @Override
            public void beforeChildAddition(@NotNull PsiTreeChangeEvent event) {
                super.beforeChildAddition(event);
            }
        }, project);

        for (PsiPackage subPsiPackage : psiPackages) {
            this.addMetric(new PackageCodeLineMetric(subPsiPackage));
        }

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

    /**
     * Returns the root package(s) in the source directory of a project. The default package will not be considered, as
     * it includes all Java classes. Note that classes in the default package (i.e., having no package statement) will
     * be ignored for this assignment. To be completed, this case must be separately handled.
     *
     * @param project a project
     * @return a set of root packages
     */
    private Set<PsiPackage> getRootPackages(Project project) {
        final Set<PsiPackage> rootPackages = new HashSet<>();
        var visitor = new PsiElementVisitor() {
            @Override
            public void visitDirectory(@NotNull PsiDirectory dir) {
                final var psiPackage = JavaDirectoryService.getInstance().getPackage(dir);
                if (psiPackage != null && !PackageUtil.isPackageDefault(psiPackage))
                    rootPackages.add(psiPackage);
                else
                    Arrays.stream(dir.getSubdirectories()).forEach(sd -> sd.accept(this));
            }
        };

        var rootManager = ProjectRootManager.getInstance(project);
        var psiManager = PsiManager.getInstance(project);

        Arrays.stream(rootManager.getContentSourceRoots())
                .map(psiManager::findDirectory)
                .filter(Objects::nonNull)
                .forEach(dir -> dir.accept(visitor));

        return rootPackages;
    }
}
