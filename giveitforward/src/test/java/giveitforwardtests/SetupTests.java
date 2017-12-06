package giveitforwardtests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SetupTests {

    public static SessionFactory getFactory(){
        SessionFactory factory;
        try {
            AnnotationConfiguration config = new AnnotationConfiguration().configure();
            factory = config.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return factory;
    }
}
