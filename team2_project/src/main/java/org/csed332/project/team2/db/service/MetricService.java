package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.model.Model;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Defines methods to use database.
 * All methods interact with embedded database.
 */
public class MetricService {
    /**
     * Gets list of metric data by metric name with limit.
     * Each element of list consists of metric data that share same CalcHistoryModel.
     * Each key string of map of return type represent class name, method name, and type.
     * Value of third map represents value of metric.
     * Max size of returned list equals limit.
     *
     * @param metric the metric name
     * @param limit  the limit
     * @return the list of the metric data
     */
    public static List<Map<String, Map<String, Map<String, Double>>>> getMetric(String metric, Integer limit) {
        List<Map<String, Map<String, Map<String, Double>>>> result = new ArrayList<>();
        List<CalcHistoryModel> historyModels = query(metric, limit);

        for (CalcHistoryModel history : historyModels) {
            Map<String, Map<String, Map<String, Double>>> subResult = new HashMap<>();
            Collection<MetricModel> metricModels = history.getMetricModels();

            for (MetricModel m : metricModels) {
                if (!subResult.containsKey(m.getClassName())) {
                    subResult.put(m.getClassName(), new HashMap<>());
                }

                if (!subResult.get(m.getClassName()).containsKey(m.getMethodName())) {
                    subResult.get(m.getClassName()).put(m.getMethodName(), new HashMap<>());
                }

                subResult.get(m.getClassName()).get(m.getMethodName()).put(m.getType(), m.getFigure());
                result.add(Collections.unmodifiableMap(subResult));
            }
        }

        return result;
    }

    /**
     * Gets the most recent metric data by metric name.
     * The element consists of metric data that share same CalcHistoryModel.
     * Each key string of map of return type represent class name, method name, and type.
     * Value of third map represents value of metric.
     *
     * @param metric the metric name
     * @return the metric data
     */
    public static Optional<Map<String, Map<String, Map<String, Double>>>> getMetric(String metric) {
        return Optional.ofNullable(getMetric(metric, 1).get(0));
    }

    /**
     * Compare two recent metric data about a given metric name and return older value minus newer value.
     * Each key string of map of return type represent class name, method name, and type.
     * Value of third map represents the difference of two metric value.
     * Returns empty map if there are no two recent data.
     *
     * @param metric the metric name
     * @return the metric difference data
     */
    public static Map<String, Map<String, Map<String, Double>>> compareMetric(String metric) {
        Map<String, Map<String, Map<String, Double>>> result = new HashMap<>();
        List<Map<String, Map<String, Map<String, Double>>>> metrics = getMetric(metric, 2);

        if (metrics.size() != 2) {
            return Map.of();
        }

        Map<String, Map<String, Map<String, Double>>> present = metrics.get(0);
        Map<String, Map<String, Map<String, Double>>> past = metrics.get(1);

        for (String className : present.keySet()) {
            if (!past.containsKey(className)) {
                break;
            }

            for (String methodName : present.get(className).keySet()) {
                if (!past.get(className).containsKey(methodName)) {
                    break;
                }

                for (Map.Entry<String, Double> type : present.get(className).get(methodName).entrySet()) {
                    Double pastFigure = past.get(className).get(methodName).get(type.getKey());

                    if (pastFigure == null) {
                        break;
                    }

                    if (!result.containsKey(className)) {
                        result.put(className, new HashMap<>());
                    }

                    if (!result.get(className).containsKey(methodName)) {
                        result.get(className).put(methodName, new HashMap<>());
                    }

                    result.get(className).get(methodName).put(type.getKey(), pastFigure - type.getValue());
                }
            }
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * Generate a new CalcHistoryModel with given metric name and save it to database.
     *
     * @param metric the metric name
     * @return the CalcHistoryModel object
     */
    public static CalcHistoryModel generateCalcHistoryModel(String metric) {
        CalcHistoryModel c = new CalcHistoryModel();
        c.setMetric(metric);

        saveOrUpdate(c);

        return c;
    }

    /**
     * Creates a new MetricModel for given metric data and save it to database.
     * Returns created MetricModel object.
     *
     * @param metric           the metric name
     * @param className        the class name
     * @param methodName       the method name
     * @param type             the type
     * @param figure           the figure
     * @param calcHistoryModel the CalcHistoryModel object that will contain new MetricModel object
     * @return the MetricModel object
     */
    public static MetricModel addMetric(String metric, String className, String methodName, String type, Double figure, CalcHistoryModel calcHistoryModel) {
        MetricModel m = new MetricModel();
        m.setMetric(metric);
        m.setClassName(className);
        m.setMethodName(methodName);
        m.setType(type);
        m.setFigure(figure);

        calcHistoryModel.addMetricModel(m);

        saveOrUpdate(m);
        saveOrUpdate(calcHistoryModel);
        return m;
    }

    /**
     * Query database to get list of CalcHistoryModel with given metric name with limit.
     * The list is sorted by timestamp, starting with the most recent one.
     * Max size of returned list equals limit.
     *
     * @param metric the metric name
     * @param limit  the limit
     * @return the list of CalcHistoryModel objects
     */
    public static List<CalcHistoryModel> query(String metric, Integer limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CalcHistoryModel> criteria = builder.createQuery(CalcHistoryModel.class);

            Root<CalcHistoryModel> root = criteria.from(CalcHistoryModel.class);

            criteria.select(root);
            criteria.orderBy(builder.desc(root.get("timestamp")));

            if (metric != null) {
                criteria.where(builder.equal(root.get("metric"), metric));
            }

            TypedQuery<CalcHistoryModel> query = session.createQuery(criteria);

            if (limit != null) {
                query.setMaxResults(limit);
            }

            return query.getResultList();
        }
    }

    /**
     * Query database to get the most recent CalcHistoryModel with given metric name.
     *
     * @param metric the metric
     * @return the Optional of CalcHistoryModel
     */
    public static Optional<CalcHistoryModel> query(String metric) {
        return Optional.ofNullable(query(metric, 1).get(0));
    }

    /**
     * Query database to get list of metric data filtered with parameters.
     * If a parameter is empty, the data is not filtered with that parameter.
     * The list is sorted by created date, starting with the most recent one.
     * Max size of returned list equals limit.
     *
     * @param metric     the metric name
     * @param className  the class name
     * @param methodName the method name
     * @param type       the type
     * @param history    the CalcHistoryModel object
     * @param limit      the limit
     * @return the list of metric data
     */
    public static List<MetricModel> query(String metric, String className, String methodName, String type, CalcHistoryModel history, Integer limit) {
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

            if (methodName != null) {
                criteria.where(builder.equal(root.get("methodName"), methodName));
            }

            if (type != null) {
                criteria.where(builder.equal(root.get("type"), type));
            }

            if (history != null) {
                criteria.where(builder.or(
                        root.get("history_id").isNull(),
                        builder.equal(root.get("history_id"), history.getId())
                ));
            }

            TypedQuery<MetricModel> query = session.createQuery(criteria);

            if (limit != null) {
                query.setMaxResults(limit);
            }

            return query.getResultList();
        }
    }

    /**
     * Query database to get a metric data filtered with parameters.
     * If a parameter is empty, the data is not filtered with that parameter.
     *
     * @param metric     the metric name
     * @param className  the class name
     * @param methodName the method name
     * @param type       the type
     * @param history    the CalcHistoryModel object
     * @return the metric data
     */
    public static Optional<MetricModel> query(String metric, String className, String methodName, String type, CalcHistoryModel history) {
        return Optional.ofNullable(query(metric, className, methodName, type, history, 1).get(0));
    }

    private static void saveOrUpdate(Model m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(m);
            session.getTransaction().commit();
        }
    }

    private static void remove(Model m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(m);
            session.getTransaction().commit();
        }
    }
}
