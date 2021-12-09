package org.csed332.project.team2.metrics;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;

import java.util.*;
import java.util.stream.Collectors;

public class MetricValue {
    private final String id;
    private final String name;

    public MetricValue(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Map<String, Double> get() {
        List<MetricModel> metrics = MetricModelService.getMetrics(id, name);

        return metrics.stream().collect(Collectors.toMap(MetricModel::getType, MetricModel::getFigure));
    }

    public Optional<Double> get(String key) {
        List<MetricModel> metrics = MetricModelService.getMetrics(id, name, key);

        if (metrics.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(MetricModelService.getMetrics(id, name, key).get(0).getFigure());
    }

    public void set(String key, double value) {
        MetricModelService.updateMetric(id, name, key, value);
    }
}
