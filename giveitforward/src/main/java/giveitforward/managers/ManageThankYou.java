package giveitforward.managers;

import giveitforward.models.Request;
import giveitforward.models.ThankYou;
import giveitforward.models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import java.sql.Timestamp;
import java.util.List;


public class ManageThankYou {


    public static void main(String[] args) {

        ManageThankYou mty = new ManageThankYou();
        mty.createThankYou(new ThankYou(54, "Thanks for helping out!", null));
    }

    public ManageThankYou(){

    }

    /**
     * Creates a new ThankYou.
     * @return the created thankYou or null if an error occurred.
     */
    public ThankYou createThankYou(ThankYou thankyou) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

			thankyou.setDate(new Timestamp(System.currentTimeMillis()));
            session.save(thankyou);
            session.flush();
            t.commit();
        }
        catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return null;

        } finally {
            session.close();
        }

        ManageRequest reqManager = new ManageRequest();
		ManageUser userManager = new ManageUser();
        Request r = reqManager.getRequestByRid("" + thankyou.getRid()).get(0);
		User requestor = userManager.getUserfromUID(r.getRequestor().getUid());
		ManageNotification noteManager = new ManageNotification();
		noteManager.createNotification("You received a THANK YOU from " + requestor.getUsername(), r.getDuid(), 2, r.getRid());

        return thankyou;
    }

    /**
     * Updates the ThankYou with the new information provided.
     * @param thankyou
     * @return
     */
    public ThankYou updateThankYou(ThankYou thankyou) {
		ThankYou currentThankYou = getThankYouByRid(thankyou.getRid());
		thankyou.setDate(currentThankYou.getDate());

		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try {
			t = session.beginTransaction();
			session.update(thankyou);
			session.flush();
			t.commit();
		}
		catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			System.out.println("ROLLBACK");
			e.printStackTrace();
			return null;

		} finally {
			session.close();
		}
		return thankyou;
    }

    /**
     * Deletes the ThankYou (sets the removed timestamp doesn't actually delete?)
     * @return true if the ThankYou was successfully removed.
     */
    public ThankYou deleteThankYou(ThankYou thankyou) {
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try {
			t = session.beginTransaction();
			session.delete(thankyou);
			session.flush();
			t.commit();
		}
		catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			System.out.println("ROLLBACK");
			e.printStackTrace();
			return null;

		} finally {
			session.close();
		}
		return thankyou;
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

    public List<ThankYou> getAllThankYous() {
		String query = "select t from ThankYou t order by t.date desc";
		return makeListQuery(query);
    }

    public ThankYou getThankYouByRid(int rid){
    	String query = "select t from ThankYou t where rid = " + rid;
    	List<ThankYou> result = makeListQuery(query);
    	if (result.size() != 1){
    		return null;
		}
		return result.get(0);
	}

    List<ThankYou> makeListQuery(String query){
		Session session = SessionFactorySingleton.getFactory().openSession();

		Transaction t = null;
		List<ThankYou> thankYous = null;

		try {
			t = session.beginTransaction();
			thankYous = (List<ThankYou>) session.createQuery(query).list();
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
			return thankYous;
		}
	}
}
