package org.csed332.project.team2.db.model;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "MetricModels")
public class MetricModel {
    private Long id;
    private String metric;
    private String className;
    private Double figure;

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

    public Double getFigure() {
        return this.figure;
    }

    public void setFigure(Double figure) {
        this.figure = figure;
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
        return Objects.hash(this.id, this.className, this.figure);
    }
}
