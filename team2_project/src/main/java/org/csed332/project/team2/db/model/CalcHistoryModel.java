package org.csed332.project.team2.db.model;

import javax.persistence.*;
import java.util.*;

/**
 * Groups multiple MetricModel objects.
 * When metric data is calculated, those with same metric name are grouped into one CalcHistoryModel.
 */
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

    /**
     * Gets id (for database).
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id (for database).
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets name of the metric.
     *
     * @return the metric name
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Sets name of the metric.
     *
     * @param metric the metric name
     */
    public void setMetric(String metric) {
        this.metric = metric;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets collection of MetricModel.
     *
     * @return the MetricModel
     */
    public Collection<MetricModel> getMetricModels() {
        return Collections.unmodifiableCollection(metricModels);
    }

    /**
     * Add MetricModel.
     *
     * @param metricModel MetricModel object
     */
    public void addMetricModel(MetricModel metricModel) {
        metricModels.add(metricModel);
        metricModel.setHistory(this);
    }

    /**
     * Remove MetricModel.
     *
     * @param metricModel MetricModel object
     */
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
