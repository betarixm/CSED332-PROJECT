package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.util.HibernateUtil;

import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class MetricModelService {
    public static MetricModel getMetricById(Long id) {
        MetricModel m;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            m = s.get(MetricModel.class, id);
        }

        return m;
    }

    public static MetricModel saveMetric(String metric, String className, double figure) {
        MetricModel m = new MetricModel();
        m.setMetric(metric);
        m.setClassName(className);
        m.setFigure(figure);

        MetricModelService.save(m);

        return m;
    }

    public static List<MetricModel> query(String metric, String className, Date start, Date end, Integer limit) {
        return List.of();
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
