package giveitforward.managers;

import giveitforward.models.*;
import org.hibernate.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManageRequest {


    /**
     * Used for quick testing.
     */
    public static void main(String[] args) {

        ManageRequest mr = new ManageRequest();
//        mr.fulfillRequest(3, 4);
//        Request r = mr.createRequest("NEW REQUEST", 20.00, "image", 1);
//        System.out.println("Donations COUNT: " + mr.getCountDonationsByUID(1));
//        System.out.println("Requests COUNT: " + mr.getCountRequestsByUID(1));

//        List<Request> req = mr.getAllRequests();
//        System.out.println(req.size());
//
//        for (Request r : req) {
//
//            System.out.println(r.asString());
//        }

//        for(Request r : mr.getRequestsFilterByRequestUid("1")){
//            System.out.println(r.asString());
//            /* returns
//                rid: 1, amount: 20.0.
//                rid: 3, amount: 20.0.
//             */
//         }
//
//        for(Request r : mr.getRequestsFilterByDonateUid("4")){
////            System.out.println(r.asString());
//            System.out.println(r.asJSON());
//            /* returns
//                rid: 2, amount: 35.0, thankYou: true
//             */
//        }
//
//
//        for(Request r : mr.getRequestsFilterByRequestUidOpen("1")){
//            System.out.println(r.asJSON());
//            /* returns
//                rid: 1, amount: 20.0
//             */
//        }

//		Request request = new Request();
//		request.setRid(73);
//		mr.deleteRequest(request);

        mr.fulfillRequest(54, 1);
    }

    public ManageRequest() {
    }

    /**
     * Creates a new request.
     *
     * @return
     */
    public Request createRequest(Request req) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        ManageUser m = new ManageUser();


        try {

            t = session.beginTransaction();
            User ruser = m.getUserfromUID(req.getRequestor().getUid());
            req.setRequestor(ruser);
            req.setRequesttime(new Timestamp(System.currentTimeMillis()));
            RequestTag tag1 = req.getTag1();
            if (tag1 != null) {
                RequestTag tag1_1 = new ManageRequestTag().getTagByTagname(tag1.getRequestTagname());
                req.setTag1(tag1_1);
            }

            RequestTag tag2 = req.getTag2();
            if (tag2 != null) {
                RequestTag tag2_2 = new ManageRequestTag().getTagByTagname(tag2.getRequestTagname());
                req.setTag1(tag2_2);
            }


            session.save(req);
            session.flush();
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return req;
        } finally {
            session.close();
        }
        System.out.println("successfully added request");
        return req;
    }

    /**
     * Fulfills a request
     *
     * @return true if the transaction was successfully completed.
     */
    public boolean fulfillRequest(int rid, int duid) {
        Session s = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        Request r;
        try {
            t = s.beginTransaction();
            r = (Request) s.get(Request.class, rid);
            r.setDuid(duid);
            r.setDonateTime(new Timestamp(System.currentTimeMillis()));
            s.update(r);
            s.flush();
            t.commit();
        } catch (Exception e) {
            return false;
        } finally {
            s.close();
        }

        // Notification Side Effect:
        ManageUser userManager = new ManageUser();
        User donator = userManager.getUserfromUID(duid);
        ManageNotification noteManager = new ManageNotification();
        noteManager.createNotification("Your donation was FULFILLED by " + donator.getUsername(), r.getRequestor().getUid());

        return true;
    }

    /**
     * Updates the request with the new information provided.
     *
     * @param req - the request to update with its changed values.
     * @return -- the updated request or null on failure
     */
    public Request updateRequest(Request req) {

    	List<Request> request = getRequestByRid(req.getRid() + "");
    	if (request.size() != 1){
    		return null;
		}
		else{
    		req.setRequesttime(request.get(0).getRequesttime());
    		req.setRequestor(request.get(0).getRequestor());
		}

        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

            session.update(req);
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            session.close();
            return null;
        } finally {
            session.close();
        }
        System.out.println("successfully updated request");
        return req;
    }

    /**
     * Deletes the request
     *
     * @return the deleted request if it was successfully removed.
     */
    public Request deleteRequest(Request req) {

        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
		Request newReq = new Request();
		newReq.setRid(req.getRid());
        try {
            t = session.beginTransaction();
            session.delete(newReq);
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            session.close();
            return null;
        } finally {
            session.close();
        }
        System.out.println("successfully updated request");
        return req;
    }

    /**
     * Gets the count of all donations of a given user
     *
     * @param uid - uid of user
     * @return count of donations made, -1 if error is thrown.
     */
    public int getCountDonationsByUID(int uid) {
        String queryString = "select count(*) from Request where duid = :id";
        String paramType = "id";
        int paramVal = uid;
        return makeCountQuery(queryString, paramType, paramVal);
    }

    /**
     * Gets the count of all requests made by a given user
     *
     * @param uid - uid of user
     * @return count of requests made, -1 if error is thrown.
     */
    public int getCountRequestsByUID(int uid) {

        String queryString = "select count(*) from Request where rUser.uid = :id and duid is not null";
        String paramType = "id";
        int paramVal = uid;
        return makeCountQuery(queryString, paramType, paramVal);
    }


    /******************************* Searches/Filters ***************************/

    /**
     * @return returns all pending requests in the DB.
     */
    public List<Request> getAllRequests() {

        return makeQuery("select r from Request r order by r.requesttime desc");
    }


    public List<Request> getRequestByRid(String rid) {
        return makeQuery("select r from Request r where " +
                "r.rid = " + rid);
    }

    /**
     * @return returns all pending requests in the DB.
     */
    public List<Request> getOpenRequests() {

        return makeQuery("select r from Request r where r.duid is null order by r.requesttime desc");
    }

    /**
     * Queries the DB for requests fulfilled by the user with the given uid.
     *
     * @param dUid uid
     * @return a list of requests fulfilled by the user with the given uid.
     */
    public List<Request> getRequestsFilterByDonateUid(String dUid) {
        return makeQuery("select r from Request r where " +
                "r.duid = " + dUid + " order by r.requesttime desc");
    }

    /**
     * Queries the DB for open and fulfilled requests created by a user with the given uid.
     *
     * @param rUid
     * @return a list of open and closed requests created by a user with the given uid.
     */
    public List<Request> getRequestsFilterByRequestUid(String rUid) {

        return makeQuery("select r from Request r where r.rUser.uid = " + rUid + " order by r.requesttime desc");
    }

    /**
     * Queries the DB for open requests created by a user with the given uid.
     *
     * @param rUid
     * @return a list of open requests created by a user with the given uid.
     */
    public List<Request> getRequestsFilterByRequestUidOpen(String rUid) {
        return makeQuery("select r from Request r where r.rUser.uid = " + rUid + " and r.duid is null order by r.requesttime desc");
    }

    /**
     *
     * @param requestTags
     * @param userTags
     * @return
     */
    public List<Request> getRequestsFilterByTags(List<RequestTag> requestTags, List<UserTag> userTags, String age, String price) {
        String query = "select r from Request r";

        // Request tags
        if (requestTags != null && !requestTags.isEmpty()) {
            int firstTag = requestTags.get(0).getRequestTid();
            query += " where r.tag1 = " + firstTag + " or r.tag2 = " + firstTag;

            // Iteratively add all request tags
            for (int i = 1; i < requestTags.size(); i++) {
                int currTag = requestTags.get(i).getRequestTid();
                query += " or r.tag1 = " + currTag + " or r.tag2 = " + currTag;
            }
        }

        // User tags
        if (userTags != null && !userTags.isEmpty()) {
            // First get all uids attached to those tags
            ManageUserTag mu = new ManageUserTag();
            List<UserTagPair> uids = mu.getUsersByTags(userTags);

            // Check if we got any uids to attach to the query
            if (uids == null || uids.isEmpty()) {
                return makeQuery(query);
            }

            // If we didn't add request tags, we need to set up this query
            int uidIndex = 0;
            if (requestTags == null || requestTags.isEmpty()) {
                query += " where r.rUser = " + uids.get(uidIndex++).getUid();
            }
            // Iteratively add all user tags
            for (int i = uidIndex; i < uids.size(); i++) {
                query += " or r.rUser = " + uids.get(i).getUid();
            }
        }

        if (price.equals("low"))
            query += "order by r.amount asc";
        else if (price.equals("high"))
            query += " order by r.amount desc";

        if (!price.isEmpty()) {
            if (age.equals("old"))
                query += ", r.requesttime asc";
            else
                query += ", r.requesttime desc";
        } else {
            if (age.equals("old"))
                query += " order by r.requesttime asc";
            else
                query += " order by r.requesttime desc";
        }

        return makeQuery(query);
    }

    /********************************** Queries *******************************/

    /**
     * @param query HQL query to be performed.
     * @return a list of Requests which results from the given query.
     */
    private List<Request> makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();//factory.openSession();
        Transaction t = null;
        List<Request> r = null;

        try {
            t = session.beginTransaction();
            r = (List<Request>) session.createQuery(query).list();
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally {
            session.close();
//            factory.close();
            return r;
        }
    }

    /**
     * @param queryString    hql query to be performed.
     * @param parameterType
     * @param parameterValue
     * @return
     */
    private int makeCountQuery(String queryString, String parameterType, Object parameterValue) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery(queryString);
        query.setParameter(parameterType, parameterValue);

        int count = -1;

        Transaction t;

        try {
            t = session.beginTransaction();
            Object result = query.uniqueResult();
            t.commit();
            count = ((Long) result).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            return count;
        }
    }
}
