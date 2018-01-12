package giveitforward.managers;

import giveitforward.models.UserTag;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class ManageTag {

    private static SessionFactory factory;

    public ManageTag(){
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Creates a new tag.
     * @return
     */
    public UserTag createTag() {
        //TODO: Implement
        return null;
    }

    /**
     * Updates the tag with the new information provided.
     * @return
     */
    public UserTag updateTag(int tid) {
        //TODO: Implement
        return null;
    }

    /**
     * Deletes the tag
     * @return true if the tag was successfully removed.
     */
    public boolean deleteTag(int tid) {
        //TODO: Implement
        return false;
    }
}
