package org.csed332.project.team2.metrics;

import java.io.File;
import java.util.Set;

import com.intellij.psi.*;

public class PackageCodeLineMetric extends CompositeCodeLineMetric {

    public PackageCodeLineMetric(PsiPackage psiPackage) {
        super();

        for (PsiPackage subPsiPackage : psiPackage.getSubPackages()) {
            this.addMetric(new PackageCodeLineMetric(subPsiPackage));
        }

        for (PsiClass subPsiClass : psiPackage.getClasses()) {
            this.addMetric(new ClassCodeLineMetric(subPsiClass));
        }
    }
}
