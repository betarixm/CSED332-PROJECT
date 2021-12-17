package org.csed332.project.team2.metrics.codeline;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;

/**
 * Class for code line metric on a package.
 */
public class PackageCodeLineMetric extends CompositeCodeLineMetric {

    /**
     * Instantiates a new PackageCodeLineMetric.
     *
     * @param psiPackage PsiPackage object
     */
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
