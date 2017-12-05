package giveitforward.managers;

import giveitforwardtests.models.Request;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.List;

public class ManageRequest {

    private static SessionFactory factory;

    public ManageRequest(){
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }


    }

    //TODO - get all requests from the database
    public List<Request> getAllRequests() {
        return null;
    }
}
