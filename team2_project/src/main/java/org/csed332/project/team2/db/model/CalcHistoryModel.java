package org.csed332.project.team2.db.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "CalcHistoryModels")
public class CalcHistoryModel implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String metric;
    private Date timestamp = new Date();

    @OneToMany(targetEntity = MetricModel.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<MetricModel> metricModels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Collection<MetricModel> getMetricModels() {
        return Collections.unmodifiableCollection(metricModels);
    }

    public void addMetricModel(MetricModel metricModel) {
        metricModels.add(metricModel);
        metricModel.setHistory(this);
    }

    public void removeMetricModel(MetricModel metricModel) {
        metricModels.remove(metricModel);
        metricModel.setHistory(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        CalcHistoryModel c = (CalcHistoryModel) o;

        return this.id.equals(c.id) && this.metric.equals(c.metric);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.metric);
    }
}
