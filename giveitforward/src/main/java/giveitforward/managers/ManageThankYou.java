package giveitforward.managers;

import giveitforward.models.Request;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class ManageThankYou {

    private static SessionFactory factory;

    public ManageThankYou(){
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Creates a new ThankYou.
     * @return
     */
    public Request createThankYou() {
        //TODO: Implement
        return null;
    }

    /**
     * Updates the ThankYou with the new information provided.
     * @param rid
     * @return
     */
    public Request updateThankYou(int rid) {
        //TODO: Implement
        return null;
    }

    /**
     * Deletes the ThankYou (sets the removed timestamp doesn't actually delete?)
     * @return true if the ThankYou was successfully removed.
     */
    public boolean deleteThankYou(int rid) {
        //TODO: Implement
        //TODO: did we decide not to do this?
        return false;
    }
}
