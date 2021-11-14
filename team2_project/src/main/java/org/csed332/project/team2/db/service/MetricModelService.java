package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.util.HibernateUtil;

import org.hibernate.Session;

public class MetricModelService {
    public static MetricModel getMetricById(Long id) {
        MetricModel m;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            m = s.get(MetricModel.class, id);
        }

        return m;
    }

    public static void save(MetricModel car) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(car);
            session.getTransaction().commit();
        }
    }
}
