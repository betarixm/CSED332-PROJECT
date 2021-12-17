package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Defines methods to use database.
 * All methods interact with embedded database.
 * This class is older version. Recommend to use MetricService instead.
 */
@Deprecated
public class MetricModelService {
    /**
     * Gets metric data by id.
     *
     * @param id the id
     * @return the MetricModel object with id
     */
    public static MetricModel getMetricById(Long id) {
        MetricModel m;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            m = s.get(MetricModel.class, id);
        }

        return m;
    }

    /**
     * Gets list of metric data by name of the metric and class.
     *
     * @param metric    the metric name
     * @param className the class name
     * @return the list of the metric data
     */
    public static List<MetricModel> getMetrics(String metric, String className) {
        return query(metric, className, null, null, null);
    }

    /**
     * Gets list of metric data by name of the metric and class with limit.
     * Max size of returned list equals limit.
     *
     * @param metric    the metric name
     * @param className the class name
     * @param limit     the limit
     * @return the list of the metric data
     */
    public static List<MetricModel> getMetrics(String metric, String className, int limit) {
        return query(metric, className, null, null, limit);
    }

    /**
     * Save metric data.
     * Returns MetricModel object created from parameters.
     *
     * @param metric    the metric name
     * @param className the class name
     * @param figure    the figure
     * @return MetricModel object
     */
    public static MetricModel saveMetric(String metric, String className, double figure) {
        MetricModel m = new MetricModel();
        m.setMetric(metric);
        m.setClassName(className);
        m.setFigure(figure);

        MetricModelService.save(m);

        return m;
    }

    /**
     * Query database to get list of metric data filtered with parameters.
     * If a parameter is empty, the data is not filtered with that parameter.
     * The list is sorted by created date, starting with the most recent one.
     * Max size of returned list equals limit.
     *
     * @param metric    the metric name
     * @param className the class name
     * @param start     the start Date of created
     * @param end       the end Date of created
     * @param limit     the limit of result list
     * @return the list of the metric data
     */
    public static List<MetricModel> query(String metric, String className, Date start, Date end, Integer limit) {
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

    /**
     * Save a MetricModel object to database.
     *
     * @param m MetricModel to be saved
     */
    public static void save(MetricModel m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(m);
            session.getTransaction().commit();
        }
    }

    /**
     * Remove a MetricModel object from database.
     *
     * @param m MetricModel to be removed
     */
    public static void remove(MetricModel m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(m);
            session.getTransaction().commit();
        }
    }
}
