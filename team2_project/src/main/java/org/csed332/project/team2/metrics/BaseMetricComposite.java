package org.csed332.project.team2.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseMetricComposite extends BaseMetric {
    final private List<BaseMetric> metricList;

    public BaseMetricComposite() {
        this.metricList = new ArrayList<>();
    }

    public void addMetric(BaseMetric metricComposite) {
        this.metricList.add(metricComposite);
    }

    public List<BaseMetric> getMetricList() {
        return Collections.unmodifiableList(metricList);
    }

    @Override
    public double calculate() {
        setMetric(metricList.stream().mapToDouble(BaseMetric::calculate).sum());
        return get();
    }
}
