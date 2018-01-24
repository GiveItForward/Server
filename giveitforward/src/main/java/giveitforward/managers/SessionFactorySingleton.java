package giveitforward.managers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SessionFactorySingleton {
    // This is the instance of session factory that you are going to need to create.
    private static SessionFactory instance = null;

    private SessionFactorySingleton() {
        System.err.println("Creating singleton for session factory...");
    }

    public static SessionFactory getFactory() {
        if (instance == null) {
            System.out.println("creating new instance");
            instance = createSessionFactory();
        }
        return instance;
    }

    private static SessionFactory createSessionFactory() {
        Throwable exception = null;
        try {
            return new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object. Retrying...");
            exception = ex;
        }
        throw new ExceptionInInitializerError(exception);
    }
}
