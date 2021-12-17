package org.csed332.project.team2.metrics.maintainability;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.portability.HardcodedLineSeparatorsInspection;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.codeline.CodeLineMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;

import java.util.Map;

public class MaintainabilityMetric extends BaseMetric {

    private final CodeLineMetric codeLineMetric;
    private final HalsteadMetric halsteadVolume;
    private final CyclomaticMetric cycloMetric;

    public MaintainabilityMetric(CodeLineMetric codeLineMetric, HalsteadMetric halsteadMetric, CyclomaticMetric cyclomaticMetric) {
        super();

        this.codeLineMetric = codeLineMetric;
        this.halsteadVolume = halsteadMetric;
        this.cycloMetric = cyclomaticMetric;

        setID(Type.MAINTAINABILITY.toString());
    }

    @Override
    public double calculate() {
        double totalMetric = halsteadVolume.get() == 0 ? 0 : Math.max(0, (171 - 5.2 * Math.log(halsteadVolume.get())
                - 0.23 * (cycloMetric.get()) - 16.2 * Math.log(codeLineMetric.get())) * 100 / 171);

        setMetric(totalMetric);
        return totalMetric;
    }

    @Override
    public void save(CalcHistoryModel calc) {

    }
}