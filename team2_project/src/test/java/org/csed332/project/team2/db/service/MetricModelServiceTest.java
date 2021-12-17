package org.csed332.project.team2.db.service;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetricModelServiceTest {
    static List<MetricModel> metricModelList;

    static {
        metricModelList = new ArrayList<>();
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
            session.getTransaction().commit();
        }
        metricModelList.clear();
    }

    private MetricModel generateMetricModel() {
        MetricModel m = MetricModelService.saveMetric(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Math.random());
        metricModelList.add(m);
        return m;
    }

    private MetricModel generateMetricModel(String metric, String className) {
        MetricModel m = MetricModelService.saveMetric(metric, className, Math.random());
        metricModelList.add(m);
        return m;
    }

    private List getMetricModelFromDB(MetricModel m) {
        List list;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "from MetricModel where className = :className and metric = :metric";
            Query query = session.createQuery(hql);
            query.setParameter("className", m.getClassName()).setParameter("metric", m.getMetric());
            list = query.list();
            session.getTransaction().commit();
        }
        return list;
    }

    @Test
    public void testDBGetNull() {
        MetricModel m = MetricModelService.getMetricById(-1L);
        Assertions.assertNull(m);
    }

    @Test
    public void testDBSave() {
        MetricModel m = generateMetricModel();

        List l = getMetricModelFromDB(m);
        Assertions.assertEquals(1, l.size());

        MetricModel mNew = (MetricModel) l.get(0);
        Assertions.assertEquals(m.getCreated().getTime(), mNew.getCreated().getTime());
        Assertions.assertEquals(m.getClassName(), mNew.getClassName());
        Assertions.assertEquals(m.getMetric(), mNew.getMetric());
        Assertions.assertEquals(m.getFigure(), mNew.getFigure());
    }

    @Test
    public void testDBSaveAndGet() {
        MetricModel m = generateMetricModel();

        MetricModel mNew = MetricModelService.getMetricById(m.getId());
        Assertions.assertEquals(m.getCreated().getTime(), mNew.getCreated().getTime());
        Assertions.assertEquals(m.getClassName(), mNew.getClassName());
        Assertions.assertEquals(m.getMetric(), mNew.getMetric());
        Assertions.assertEquals(m.getFigure(), mNew.getFigure());
    }

    @Test
    public void testDBSaveAndRemove() {
        MetricModel m = generateMetricModel();

        MetricModelService.remove(m);
        metricModelList.remove(m);

        List l = getMetricModelFromDB(m);
        Assertions.assertTrue(l.isEmpty());
    }

    @Test
    public void testQuery() {
        List<MetricModel> testMetricModels = IntStream.range(0, 100).mapToObj(i -> generateMetricModel()).collect(Collectors.toList());
        MetricModel m = MetricModelService.query(testMetricModels.get(0).getMetric(), null, null, null, null).get(0);

        Assertions.assertEquals(testMetricModels.get(0), m);
    }

    @Test
    public void testGetMetrics() {
        String testMetric = "#testMetric";
        String testClass = "#testClass";
        int testSize = 100, sameSize = 10;

        List<MetricModel> testMetricModels = IntStream.range(0, testSize).mapToObj(i -> generateMetricModel()).collect(Collectors.toList());
        List<MetricModel> sameMetricModels = IntStream.range(0, sameSize).mapToObj(i -> generateMetricModel(testMetric, testClass)).collect(Collectors.toList());
        List<MetricModel> result = MetricModelService.getMetrics(testMetric, testClass);

        Collections.reverse(result); // Result is desc. order with created field.

        Assertions.assertEquals(sameMetricModels.size(), result.size());

        IntStream.range(0, sameSize).forEach(i -> {
            Assertions.assertEquals(sameMetricModels.get(i), result.get(i));
        });
    }

    @Test
    public void testGetMetricsByClassNameWithLimit() {
        String testMetric = "#testMetric";
        String testClass = "#testClass";
        int testSize = 100, sameSize = 10, limitSize = 5;

        List<MetricModel> testMetricModels = IntStream.range(0, testSize).mapToObj(i -> generateMetricModel()).collect(Collectors.toList());
        List<MetricModel> sameMetricModels = IntStream.range(0, sameSize).mapToObj(i -> generateMetricModel(testMetric, testClass)).collect(Collectors.toList());
        List<MetricModel> result = MetricModelService.getMetrics(testMetric, testClass, limitSize);

        Collections.reverse(sameMetricModels); // sameMetricModels is asc. order with created field.

        Assertions.assertEquals(limitSize, result.size());

        IntStream.range(0, limitSize).forEach(i -> {
            Assertions.assertEquals(sameMetricModels.get(i), result.get(i));
        });
    }
}
