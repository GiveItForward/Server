package giveitforward.managers;

import giveitforward.models.Request;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class ManageNotification {


    private static SessionFactory factory;

    public ManageNotification(){
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Creates a new request.
     * @return
     */
    public Request createNotification() {
        //TODO: Implement
        return null;
    }

    /**
     * Marks a request as fulfilled.
     * @return true if the transaction was successfully completed.
     */
    public boolean seenNotification() {
        //TODO: Implement
        //TODO: How do we do this?
        return false;
    }

    /**
     * Updates the request with the new information provided.
     * @return
     */
    public Request updateNotification(int nid) {
        //TODO: Implement
        return null;
    }

    /**
     * Deletes the notification
     * @return true if the notification was successfully removed.
     */
    public boolean deleteNotification(int nid) {
        //TODO: Implement
        return false;
    }
}