package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.util.HibernateUtil;

import org.hibernate.Session;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class MetricModelService {
    public static MetricModel getMetricById(Long id) {
        MetricModel m;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            m = s.get(MetricModel.class, id);
        }

        return m;
    }

    public static List<MetricModel> getMetrics(String metric, String className) {
        return query(metric, className, null, null, null, null);
    }

    public static List<MetricModel> getMetrics(String metric, String className, String type) {
        return query(metric, className, type, null, null, null);
    }

    public static List<MetricModel> getMetrics(String metric, String className, String type, int limit) {
        return query(metric, className, type, null, null, limit);
    }

    public static MetricModel saveMetric(String metric, String className, String type, double figure) {
        MetricModel m = new MetricModel();
        m.setMetric(metric);
        m.setClassName(className);
        m.setType(type);
        m.setFigure(figure);

        MetricModelService.save(m);

        return m;
    }

    public static MetricModel updateMetric(String metric, String className, String type, double figure) {
        List<MetricModel> l = query(metric, className, type, null, null, 1);
        MetricModel m;

        if (l.isEmpty()) {
            m = saveMetric(metric, className, type, figure);
        } else {
            m = l.get(0);
            m.setMetric(metric);

            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.saveOrUpdate(m);
            }
        }

        return m;
    }

    public static List<MetricModel> query(String metric, String className, String type, Date start, Date end, Integer limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MetricModel> criteria = builder.createQuery(MetricModel.class);

            Root<MetricModel> root = criteria.from(MetricModel.class);

            criteria.select(root);
            criteria.orderBy(builder.desc(root.get("created")));

            if (metric != null) {
                criteria.where(builder.equal(root.get("metric"), metric));
            }

            if (className != null) {
                criteria.where(builder.equal(root.get("className"), className));
            }

            if (type != null) {
                criteria.where(builder.equal(root.get("type"), type));
            }

            if (start != null) {
                criteria.where(builder.greaterThanOrEqualTo(root.get("created"), start));
            }

            if (end != null) {
                criteria.where(builder.lessThanOrEqualTo(root.get("created"), end));
            }

            TypedQuery<MetricModel> query = session.createQuery(criteria);

            if (limit != null) {
                query.setMaxResults(limit);
            }

            return query.getResultList();
        }
    }

    public static void save(MetricModel m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(m);
            session.getTransaction().commit();
        }
    }

    public static void remove(MetricModel m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(m);
            session.getTransaction().commit();
        }
    }
}
