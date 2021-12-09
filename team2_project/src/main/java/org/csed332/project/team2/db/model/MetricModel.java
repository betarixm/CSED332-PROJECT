package org.csed332.project.team2.db.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "MetricModels")
public class MetricModel {
    private Long id;
    private String metric;
    private String className;
    private String type;
    private Double figure;
    private Date created = new Date();
    private Date updated = new Date();

    @PreUpdate
    public void onUpdate() {
        this.updated = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetric() {
        return this.metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getFigure() {
        return this.figure;
    }

    public void setFigure(Double figure) {
        this.figure = figure;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        MetricModel m = (MetricModel) o;

        return this.id.equals(m.id) && this.className.equals(m.className) && this.figure.equals(m.figure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.className, this.type, this.figure);
    }
}
