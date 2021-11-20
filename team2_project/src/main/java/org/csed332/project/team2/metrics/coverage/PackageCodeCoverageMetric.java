package org.csed332.project.team2.metrics.coverage;

import com.intellij.psi.*;

public class PackageCodeCoverageMetric extends CompositeCodeCoverageMetric {
    PsiPackage psiPackage;

    public PackageCodeCoverageMetric(PsiPackage psiPackage) {
        super();
        this.psiPackage = psiPackage;
    }
}
