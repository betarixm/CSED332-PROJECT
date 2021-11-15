package org.csed332.project.team2;

import org.csed332.project.team2.db.model.MetricModel;
import org.csed332.project.team2.db.service.MetricModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DBTest {
    @Test
    void testDBSaveAndGet() {
        MetricModel m = new MetricModel();
        m.setClassName("ClassA");
        m.setMetric("MetricA");
        m.setFigure(1.0);

        MetricModelService.save(m);

        MetricModel mNew = MetricModelService.getMetricById(1L);
        Assertions.assertEquals("ClassA", mNew.getClassName());
        Assertions.assertEquals("MetricA", mNew.getMetric());
        Assertions.assertEquals(1.0, mNew.getFigure());

        MetricModelService.remove(mNew);
    }
}
