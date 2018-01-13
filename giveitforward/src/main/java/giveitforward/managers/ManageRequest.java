package giveitforward.managers;

import giveitforward.models.Request;
import giveitforward.models.User;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class ManageRequest {

    private static SessionFactory factory;

    public static void main(String[] args) {

        ManageRequest mr = new ManageRequest();

        //System.out.println("Donations COUNT: " + mr.getCountDonationsByUID(1));
        //System.out.println("Requests COUNT: " + mr.getCountRequestsByUID(1));

        List<Request> req = mr.getAllRequests();

//        for(Object r : req){
//
//            System.out.println(r.getClass());
//        }

        for(Request r : mr.getRequestsFilterByRequestUid("1")){
            System.out.println(r.asString());
        }

        //System.out.println("request filter by duid: " + mr.getRequestsFilterByDonateUid("4"));

    }

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
        Session session = factory.openSession();
        Transaction t = null;
        List<Request> r = null;

        try
        {
            t = session.beginTransaction();

            String s = "from Request";
            r = (List<Request>) session.createQuery(s).list();

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

    //TODO - get all requests from the database
    public List<Request> getRequestsFilterByDonateUid(String dUid) {
        return makeQuery("SELECT r.* FROM request r, user_request_pair upr WHERE r.rid = upr.rid AND " +
            "upr.uid_donate = " + dUid);
    }

    /**
     *
     * @param rUid
     * @return
     */
    public List<Request> getRequestsFilterByRequestUid(String rUid) {

        return makeQuery("SELECT r.* FROM Request r, User_Request_Pair upr WHERE r.rid = upr.rid AND " +
                "upr.uid_request = " + rUid);
    }

    private List<Request> makeQuery(String query) {
        Session session = factory.openSession();
        Transaction t = null;
        List<Request> r = null;

        try
        {
            t = session.beginTransaction();
            r = (List<Request>) session.createQuery(query).list();
            System.out.println("type? " + r.get(0).getClass());
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
            System.out.println("type? " + r.getClass());
            return r;
        }
    }

    /**
     * Gets the count of all donations of a given user
     * @param uid - uid of user
     * @return count of donations made, -1 if error is thrown.
     */
    public int getCountDonationsByUID(int uid) {
        Session session = factory.openSession();
        Query query = session.createQuery("select count(*) from UserRequestPair where uid_donate = :id");
        query.setParameter("id", uid);

        int count = -1;

        Transaction t;

        try {
            t = session.beginTransaction();
            Object result = query.uniqueResult();
            t.commit();
            count = ((Long)result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            session.close();
            factory.close();
            return count;
        }
    }

    /**
     * Gets the count of all requests made by a given user
     * @param uid - uid of user
     * @return count of requests made, -1 if error is thrown.
     */
    public int getCountRequestsByUID(int uid) {
        Session session = factory.openSession();
        Query query = session.createQuery("select count(*) from UserRequestPair where uid_request = :id");
        query.setParameter("id", uid);

        int count = -1;

        Transaction t;

        try {
            t = session.beginTransaction();
            Object result = query.uniqueResult();
            t.commit();
            count = ((Long)result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            session.close();
            factory.close();
            return count;
        }
    }
}
