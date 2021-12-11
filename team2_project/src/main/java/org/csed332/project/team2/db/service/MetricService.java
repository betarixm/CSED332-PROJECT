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

public class MetricService {
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

    public static Optional<Map<String, Map<String, Map<String, Double>>>> getMetric(String metric) {
        return Optional.ofNullable(getMetric(metric, 1).get(0));
    }

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

                    result.get(className).get(methodName).put(type.getKey(), type.getValue());
                }
            }
        }

        return Collections.unmodifiableMap(result);
    }

    public static CalcHistoryModel generateCalcHistoryModel(String metric) {
        CalcHistoryModel c = new CalcHistoryModel();
        c.setMetric(metric);

        save(c);

        return c;
    }

    public static MetricModel addMetric(String metric, String className, String methodName, String type, Double figure, CalcHistoryModel calcHistoryModel) {
        MetricModel m = new MetricModel();
        m.setMetric(metric);
        m.setClassName(className);
        m.setMethodName(methodName);
        m.setType(type);
        m.setFigure(figure);

        calcHistoryModel.addMetricModel(m);

        save(m);
        save(calcHistoryModel);

        return m;
    }

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

    public static Optional<CalcHistoryModel> query(String metric) {
        return Optional.ofNullable(query(metric, 1).get(0));
    }

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

    public static Optional<MetricModel> query(String metric, String className, String methodName, String type, CalcHistoryModel history) {
        return Optional.ofNullable(query(metric, className, methodName, type, history, 1).get(0));
    }

    private static void save(Model m) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(m);
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
