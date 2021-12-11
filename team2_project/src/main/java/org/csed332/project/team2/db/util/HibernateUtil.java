package org.csed332.project.team2.db.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.DriverManager;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(org.csed332.project.team2.db.model.MetricModel.class)
                    .addAnnotatedClass(org.csed332.project.team2.db.model.CalcHistoryModel.class)
                    .setProperty("hibernate.connection.username", "app")
                    .setProperty("hibernate.connection.password", "app")
                    .setProperty("hibernate.connection.url", "jdbc:derby:.db/metric;create=true")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("current_session_context_class", "thread")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ignore) {}
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void shutdown() {
        sessionFactory.close();
    }
}
