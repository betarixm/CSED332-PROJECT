package org.csed332.project.team2.db;

import org.csed332.project.team2.db.model.CalcHistoryModel;
import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
            String hql = "delete from MetricModel where className = :className and metric = :metric";
            Query query = session.createQuery(hql);
            for (MetricModel m : metricModelList) {
                query.setParameter("className", m.getClassName()).setParameter("metric", m.getMetric());
                query.executeUpdate();
            }

            hql = "delete from CalcHistoryModel where metric = :metric";
            query = session.createQuery(hql);
            for (CalcHistoryModel c : calcHistoryModelList) {
                query.setParameter("metric", c.getMetric());
                query.executeUpdate();
            }
            session.getTransaction().commit();
        }
        metricModelList.clear();
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
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
          20.0,
                c);
        metricModelList.add(m);
        return m;
    }

    private MetricModel generateMetricModel(CalcHistoryModel c, String metric, String className, String methodName, String type, Double figure) {
        MetricModel m = MetricService.addMetric(metric, className, methodName, type, figure, c);
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
    }

    @Test
    public void testCompareMetrics() {
    }
}
