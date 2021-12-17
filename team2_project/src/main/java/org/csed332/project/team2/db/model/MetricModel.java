package org.csed332.project.team2.db.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Defines structure of metric data.
 */
@Entity
@Table(name = "MetricModels")
public class MetricModel implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String metric;
    private String className;
    private String methodName;
    private String type;
    private Double figure;
    private Date created = new Date();
    private Date updated = new Date();

    @ManyToOne(targetEntity = CalcHistoryModel.class, fetch = FetchType.LAZY)
    private CalcHistoryModel history;

    /**
     * On update.
     */
    @PreUpdate
    public void onUpdate() {
        this.updated = new Date();
    }

    /**
     * Gets id (for database).
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
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
        return this.metric;
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
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets method name.
     *
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets method name.
     *
     * @param methodName the method name
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets figure.
     *
     * @return the figure
     */
    public Double getFigure() {
        return this.figure;
    }

    /**
     * Sets figure.
     *
     * @param figure the figure
     */
    public void setFigure(Double figure) {
        this.figure = figure;
    }

    /**
     * Gets created time.
     *
     * @return the created time
     */
    public Date getCreated() {
        return this.created;
    }

    /**
     * Sets created time.
     *
     * @param created the created time
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets updated time.
     *
     * @return the updated time
     */
    public Date getUpdated() {
        return this.updated;
    }

    /**
     * Sets updated time.
     *
     * @param updated the updated time
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Gets CalcHistoryModel connected with this object.
     *
     * @return CalcHistoryModel object
     */
    public CalcHistoryModel getHistory() {
        return history;
    }

    /**
     * Sets CalcHistoryModel for this object.
     *
     * @param history CalcHistoryModel object
     */
    public void setHistory(CalcHistoryModel history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        MetricModel m = (MetricModel) o;

        return this.id.equals(m.id) && this.className.equals(m.className) && this.figure.equals(m.figure) && Objects.equals(this.history, m.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.className, this.figure, this.history);
    }
}
