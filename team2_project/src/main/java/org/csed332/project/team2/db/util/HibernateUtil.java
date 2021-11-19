package org.csed332.project.team2.db.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private final static String configFile = "hibernate.cfg.xml";

    static {
        try {
            sessionFactory = new Configuration().configure(configFile).buildSessionFactory();
        } catch (Throwable ignore) {
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void shutdown() {
        sessionFactory.close();
    }
}
