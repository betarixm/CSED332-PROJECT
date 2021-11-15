package org.csed332.project.team2;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

public class DBTest {
    private MetricModel getExampleMetricModel() {
        MetricModel m = new MetricModel();
        m.setClassName("ClassA");
        m.setMetric("MetricA");
        m.setFigure(1.0);
        return m;
    }

    @BeforeAll
    public static void emptyDBBefore() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            List l = session.createQuery("from MetricModel ").list();
            if (!l.isEmpty()) {
                session.createQuery("delete from MetricModel ").executeUpdate();
            }
            session.getTransaction().commit();
        }
    }

    @Test
    public void testDBGetNull() {
        MetricModel m = MetricModelService.getMetricById(1234L);
        Assertions.assertNull(m);
    }

    @Test
    public void testDBSave() {
        MetricModel m = getExampleMetricModel();
        MetricModelService.save(m);

        List l;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            l = session.createQuery("from MetricModel ").list();
            session.getTransaction().commit();
        }

        Assertions.assertEquals(1, l.size());

        MetricModel mNew = (MetricModel)l.get(0);
        Assertions.assertEquals(m.getCreated().getTime(), mNew.getCreated().getTime());
        Assertions.assertEquals("ClassA", mNew.getClassName());
        Assertions.assertEquals("MetricA", mNew.getMetric());
        Assertions.assertEquals(1.0, mNew.getFigure());
    }

    @Test
    public void testDBSaveAndGet() {
        MetricModel m = getExampleMetricModel();

        MetricModelService.save(m);

        MetricModel mNew = MetricModelService.getMetricById(m.getId());
        Assertions.assertEquals(m.getCreated().getTime(), mNew.getCreated().getTime());
        Assertions.assertEquals("ClassA", mNew.getClassName());
        Assertions.assertEquals("MetricA", mNew.getMetric());
        Assertions.assertEquals(1.0, mNew.getFigure());
    }

    @Test
    public void testDBSaveAndRemove() {
        MetricModel m = getExampleMetricModel();

        MetricModelService.save(m);

        MetricModelService.remove(m);

        List l;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            l = session.createQuery("from MetricModel ").list();
            session.getTransaction().commit();
        }
        Assertions.assertTrue(l.isEmpty());
    }

    @AfterEach
    public void emptyDBAfter() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("delete from MetricModel ").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
