package org.csed332.project.team2.metrics.maintainability;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.metrics.BaseMetric;
import org.csed332.project.team2.metrics.cyclomatic.CyclomaticMetric;
import org.csed332.project.team2.metrics.halstead.HalsteadMetric;

import java.util.Map;

public class MaintainabilityMetric extends BaseMetric {

    private final Map<String, Map<PsiMethod, Double>> halsteadVolumes;
    private final Map<String, Map<PsiMethod, Double>> cycloMetrics;

    public MaintainabilityMetric(PsiElement element, HalsteadMetric halsteadMetric, CyclomaticMetric cyclomaticMetric) {
        super();
        assert (halsteadMetric.getType().equals("VOLUME"));
        assert (halsteadMetric.getPsiElement().equals(element));
        assert (cyclomaticMetric.getPsiElement().equals(element));

        this.halsteadVolumes = halsteadMetric.getMetrics();
        this.cycloMetrics = cyclomaticMetric.getMetrics();

        setID(Type.MAINTAINABILITY.toString());
    }

    public MaintainabilityMetric(Project project, HalsteadMetric halsteadMetric, CyclomaticMetric cyclomaticMetric) {
        super();
        assert (halsteadMetric.getType().equals("VOLUME"));
        assert (halsteadMetric.getProject().equals(project));
        assert (cyclomaticMetric.getProject().equals(project));

        this.halsteadVolumes = halsteadMetric.getMetrics();
        this.cycloMetrics = cyclomaticMetric.getMetrics();

        setID(Type.MAINTAINABILITY.toString());
    }

    @Override
    public double calculate() {
        Map<String, Map<PsiMethod, Double>> metrics = halsteadVolumes;
        double totalMetric = 0;
        for (String _class : metrics.keySet()) {

            for (PsiMethod _method : metrics.get(_class).keySet()) {
                double halsteadVolume = halsteadVolumes.get(_class).get(_method);
                double cycloMetric = cycloMetrics.get(_class).get(_method);

                int codeLine = _method.getText().split("\n").length;

                double calculatedMI = Math.max(0, 171 - 5.2 * Math.log(halsteadVolume) - 0.23 * (cycloMetric) - 16.2 * Math.log(codeLine) * 100 / 171);

                setMetric(calculatedMI, _class, _method);
                totalMetric += calculatedMI;
            }
        }

        return totalMetric;
    }

    @Override
    public void save(CalcHistoryModel calc) {
        Map<String, Map<PsiMethod, Double>> metrics = getMetrics();
        for (String _class : metrics.keySet()) {
            for (PsiMethod _method : metrics.get(_class).keySet()) {
                Double _figure = metrics.get(_class).get(_method);
                MetricService.addMetric(getID(), _class, _method.getName(), "", _figure, calc);
            }
        }
    }
}