package org.csed332.project.team2.db;

import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


public class MetricServiceTest {
    static List<MetricModel> metricModelList;
    static List<CalcHistoryModel> calcHistoryModelList;

    static {
        metricModelList = new ArrayList<>();
        calcHistoryModelList = new ArrayList<>();
    }

    @AfterEach
    public void afterEach() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            for (CalcHistoryModel c : calcHistoryModelList) {
                session.remove(c);
            }
            session.getTransaction().commit();
        }
        metricModelList.clear();
        calcHistoryModelList.clear();
    }

    private CalcHistoryModel generateCalcHistoryModel() {
        CalcHistoryModel c = MetricService.generateCalcHistoryModel(UUID.randomUUID().toString());
        calcHistoryModelList.add(c);
        return c;
    }

    private CalcHistoryModel generateCalcHistoryModel(String metric) {
        CalcHistoryModel c = MetricService.generateCalcHistoryModel(metric);
        calcHistoryModelList.add(c);
        return c;
    }

    private MetricModel generateMetricModel(CalcHistoryModel c) {
        MetricModel m = MetricService.addMetric(
                c.getMetric(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                20.0,
                c);
        metricModelList.add(m);
        return m;
    }

    private MetricModel generateMetricModel(CalcHistoryModel c, String className, String methodName, String type, Double figure) {
        MetricModel m = MetricService.addMetric(c.getMetric(), className, methodName, type, figure, c);
        metricModelList.add(m);
        return m;
    }

    @Test
    public void testGenerateCalcHistoryModel() {
        CalcHistoryModel c = generateCalcHistoryModel();
        Assertions.assertNotNull(c);
    }

    @Test
    public void testAddMetric() {
        CalcHistoryModel c = generateCalcHistoryModel();
        MetricModel m = generateMetricModel(c);
        Assertions.assertNotNull(m);
    }

    @Test
    public void testGetMetric() {
        CalcHistoryModel c = generateCalcHistoryModel();
        MetricModel m = generateMetricModel(c);

        Optional<Map<String, Map<String, Map<String, Double>>>> metricOptional = MetricService.getMetric(m.getMetric());
        Assertions.assertNotNull(metricOptional);
        Assertions.assertTrue(metricOptional.isPresent());
        Assertions.assertNotNull(metricOptional.get());
        Assertions.assertEquals(1, metricOptional.get().size());
    }

    @Test
    public void testCompareMetrics() {
        final String className = "classA";
        final String methodName = "methodB";
        final String type = "default";
        CalcHistoryModel c = generateCalcHistoryModel();
        MetricModel m1 = generateMetricModel(c, className, methodName, type, 400.0);
        MetricModel m2 = generateMetricModel(c, className, methodName, type, 440.0);

        Map<String, Map<String, Map<String, Double>>> metric = MetricService.compareMetric(c.getMetric());
        Assertions.assertNotNull(metric);
        Assertions.assertEquals(1, metric.size());
        Assertions.assertEquals(440, metric.get(className).get(methodName).get(type));
    }
}
