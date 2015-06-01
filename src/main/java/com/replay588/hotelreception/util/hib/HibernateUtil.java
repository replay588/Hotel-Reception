package com.replay588.hotelreception.util.hib;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * HibernateUtil
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil(){
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration config = new AnnotationConfiguration()
                    .configure(new File("E:\\Git\\Release\\Hotel-Reception\\src\\main\\resources\\hibernate.cfg.xml"));
            return config.buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
