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

    /**
     * Used for quick testing.
     */
    public static void main(String[] args) {

        ManageRequest mr = new ManageRequest();

//        System.out.println("Donations COUNT: " + mr.getCountDonationsByUID(1));
//        System.out.println("Requests COUNT: " + mr.getCountRequestsByUID(1));
//
//        List<Request> req = mr.getAllRequests();
//        for(Request r : req){
//
//            System.out.println(r.asString());
//        }
//
//        for(Request r : mr.getRequestsFilterByRequestUid("1")){
//            System.out.println(r.asString());
//            /* returns
//                rid: 1, amount: 20.0.
//                rid: 3, amount: 20.0.
//             */
//         }
//
//        for(Request r : mr.getRequestsFilterByDonateUid("4")){
//            System.out.println(r.asString());
//            /* returns
//                rid: 2, amount: 35.0.
//             */
//        }

        for(Request r : mr.getRequestsFilterByRequestUidOpen("1")){
            System.out.println(r.asString());
        }

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

    /**
     * Gets the count of all donations of a given user
     * @param uid - uid of user
     * @return count of donations made, -1 if error is thrown.
     */
    public int getCountDonationsByUID(int uid) {
        String queryString = "select count(*) from UserRequestPair where uid_donate = :id";
        String paramType = "id";
        int paramVal = uid;
        return makeCountQuery(queryString, paramType, paramVal);
    }

    /**
     * Gets the count of all requests made by a given user
     * @param uid - uid of user
     * @return count of requests made, -1 if error is thrown.
     */
    public int getCountRequestsByUID(int uid) {
        String queryString = "select count(*) from UserRequestPair where uid_request = :id";
        String paramType = "id";
        int paramVal = uid;
        return makeCountQuery(queryString, paramType, paramVal);
    }


    /******************************* Searches/Filters ***************************/

    /**
     * @return returns all pending requests in the DB.
     */
    public List<Request> getAllRequests() {
        //TODO: Fix this, should only return pending requests.
        return makeQuery("from Request");
    }

    /**
     * Queries the DB for requests fulfilled by the user with the given uid.
     * @param dUid uid
     * @return a list of requests fulfilled by the user with the given uid.
     */
    public List<Request> getRequestsFilterByDonateUid(String dUid) {
        return makeQuery("select r from Request r, UserRequestPair upr where r.rid = upr.id.rid and " +
            "upr.uidDonate = " + dUid);
    }

    /**
     * Queries the DB for open and fulfilled requests created by a user with the given uid.
     * @param rUid
     * @return a list of open and closed requests created by a user with the given uid.
     */
    public List<Request> getRequestsFilterByRequestUid(String rUid) {

        return makeQuery("select r from Request r, UserRequestPair upr where r.rid = upr.id.rid and " +
                "upr.id.uidRequest = " + rUid);
    }

    /**
     * Queries the DB for open requests created by a user with the given uid.
     * @param rUid
     * @return a list of open requests created by a user with the given uid.
     */
    public List<Request> getRequestsFilterByRequestUidOpen(String rUid) {
        //SQL: select r.* from request r, user_request_pair upr where r.rid = upr.rid and upr.uid_request = 5 and upr.uid_donate is null;
        return makeQuery("select r from Request r, UserRequestPair upr where r.rid = upr.id.rid and " +
                "upr.id.uidRequest = " + rUid + " and upr.uidDonate is null");
    }


    /********************************** Queries *******************************/

    /**
     * @param query HQL query to be performed.
     * @return a list of Requests which results from the given query.
     */
    private List<Request> makeQuery(String query) {
        Session session = factory.openSession();
        Transaction t = null;
        List<Request> r = null;

        try
        {
            t = session.beginTransaction();
            r = (List<Request>) session.createQuery(query).list();
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

    /**
     *
     * @param queryString hql query to be performed.
     * @param parameterType
     * @param parameterValue
     * @return
     */
    private int makeCountQuery(String queryString, String parameterType, Object parameterValue) {
        Session session = factory.openSession();
        Query query = session.createQuery(queryString);
        query.setParameter(parameterType, parameterValue);

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
