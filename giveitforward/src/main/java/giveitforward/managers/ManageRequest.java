package giveitforward.managers;

import giveitforward.models.Request;
import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
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

    /**
     * Creates a new request.
     * @return
     */
    public Request createRequest() {
        //TODO: Implement
        return null;
    }

    /**
     * Marks a request as fulfilled.
     * @return true if the transaction was successfully completed.
     */
    public boolean fulfillRequest() {
        //TODO: Implement
        return false;
    }

    /**
     * Updates the request with the new information provided.
     * @param rid
     * @return
     */
    public Request updateRequest(int rid) {
        //TODO: Implement
        return null;
    }

    /**
     * Deletes the request
     * @return true if the request was successfully removed.
     */
    public boolean deleteRequest(int rid) {
        //TODO: Implement
        //TODO: did we decide not to do this?
        return false;
    }


    //TODO - get all requests from the database
    public List<Request> getAllRequests() {
        return makeQuery("SELECT * FROM request;");
    }

    //TODO - get all requests from the database
    public List<Request> getRequestsFilterByDonateUid(String dUid) {
        return makeQuery("SELECT r.* FROM request r, user_request_pair upr WHERE r.rid = upr.rid AND " +
            "upr.uid_donate = " + dUid + ";");
    }

    /**
     *
     * @param rUid
     * @return
     */
    public List<Request> getRequestsFilterByRequestUid(String rUid) {

        return makeQuery("SELECT r.* FROM request r, user_request_pair upr WHERE r.rid = upr.rid AND " +
                "upr.uid_request = " + rUid + ";");
    }

    private List<Request> makeQuery(String query) {
        Session session = factory.openSession();
        Transaction t = null;
        List<Request> r = null;

        try
        {
            t = session.beginTransaction();
            r = (List<Request>) session.createSQLQuery(query).list();

            t.commit();
        } catch (Exception e)
        {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally
        {
            session.close();
            factory.close();
            return r;
        }
    }
}
