package org.csed332.project.team2;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DBTest {
    static List<MetricModel> metricModelList;

    static {
        metricModelList = new ArrayList<>();
    }

    @AfterAll
    public static void afterAll() {
        metricModelList.forEach(MetricModelService::remove);
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

        List<MetricModel> testMetricModels = IntStream.range(0, 100).mapToObj(i -> generateMetricModel()).collect(Collectors.toList());
        List<MetricModel> sameMetricModels = IntStream.range(0, 10).mapToObj(i -> generateMetricModel(testMetric, testClass)).collect(Collectors.toList());
        List<MetricModel> result = MetricModelService.getMetrics(testMetric, testClass);

        Assertions.assertEquals(sameMetricModels, result);
    }

    @Test
    public void testGetMetricsByClassNameWithLimit() {
        String testMetric = "#testMetric";
        String testClass = "#testClass";

        List<MetricModel> testMetricModels = IntStream.range(0, 100).mapToObj(i -> generateMetricModel()).collect(Collectors.toList());
        List<MetricModel> sameMetricModels = IntStream.range(0, 10).mapToObj(i -> generateMetricModel(testMetric, testClass)).collect(Collectors.toList());
        List<MetricModel> result = MetricModelService.getMetrics(testMetric, testClass, 5);

        Assertions.assertEquals(sameMetricModels.subList(0, 5), result);
    }
}
