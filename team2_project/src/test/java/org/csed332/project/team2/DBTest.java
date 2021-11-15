package org.csed332.project.team2;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.csed332.project.team2.db.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DBTest {
    @BeforeEach
    void emptyDB() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("delete from MetricModel ").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testDBSaveAndGet() {
        MetricModel m = new MetricModel();
        m.setClassName("ClassA");
        m.setMetric("MetricA");
        m.setFigure(1.0);

        MetricModelService.save(m);

        MetricModel mNew = MetricModelService.getMetricById(m.getId());
        Assertions.assertEquals(m.getCreated().getTime(), mNew.getCreated().getTime());
        Assertions.assertEquals("ClassA", mNew.getClassName());
        Assertions.assertEquals("MetricA", mNew.getMetric());
        Assertions.assertEquals(1.0, mNew.getFigure());

        MetricModelService.remove(mNew);
    }
}
