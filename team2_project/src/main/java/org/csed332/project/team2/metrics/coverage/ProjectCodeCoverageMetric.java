package org.csed332.project.team2.metrics.coverage;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import java.util.Set;

import static org.csed332.project.team2.metrics.project.ProjectFactory.*;

public class ProjectCodeCoverageMetric extends CompositeCodeCoverageMetric {

    Project project;

    public ProjectCodeCoverageMetric() {
        this(getActiveProject());
    }

    public ProjectCodeCoverageMetric(Project project){
        super();
        this.project = project;
        Set<PsiPackage> psiPackages = getRootPackages(this.project);

        for (PsiPackage subPsiPackage : psiPackages) {
            this.addMetric(new PackageCodeCoverageMetric(subPsiPackage));
        }
    }

    public void runTestClass(Class<?> testClass){
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(testClass);
    }

    public double calculateForClass(Class<?> testClass){
        // TODO: implement this
        return 0;
    }

    @Override
    public double calculate() {
        // TODO: implement this
        return super.calculate();
    }
}
