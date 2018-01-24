package giveitforward.managers;

import giveitforward.models.Request;
import giveitforward.models.ThankYou;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import java.sql.Timestamp;

public class ManageThankYou {


    public static void main(String[] args) {

        ManageThankYou mty = new ManageThankYou();
        mty.deleteThankYou(1);
    }

    public ManageThankYou(){

    }

    /**
     * Creates a new ThankYou.
     * @return
     */
    public ThankYou createThankYou(int rid, String note, String image) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        ThankYou ty = null;

        try {
            t = session.beginTransaction();

            ty = new ThankYou(rid, note, image);
            ty.setDate(new Timestamp(System.currentTimeMillis()));
            session.save(ty);
            session.flush();
            t.commit();
        }
        catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();

        } finally {
            session.close();
        }
        return ty;
    }

    /**
     * Updates the ThankYou with the new information provided.
     * @param rid
     * @return
     */
    public ThankYou updateThankYou(int rid) {
        //TODO: Do we need this?
        return null;
    }

    /**
     * Deletes the ThankYou (sets the removed timestamp doesn't actually delete?)
     * @return true if the ThankYou was successfully removed.
     */
    public boolean deleteThankYou(int rid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery("delete ThankYou where rid = :id");
        query.setParameter("id", rid);

        return makeQuery(session, query);
    }

    /**
     * Helper method that just makes a query that has been created in a different method.
     * @param session - the current session
     * @param query - the query to be commited
     * @return true if the query was successful
     */
    private boolean makeQuery(Session session, Query query) {
        Transaction t;

        try {
            t = session.beginTransaction();
            int result = query.executeUpdate();
            t.commit();

            if (result > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally
        {
            session.close();
        }
    }
}
