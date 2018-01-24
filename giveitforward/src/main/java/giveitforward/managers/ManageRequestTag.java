package giveitforward.managers;


import giveitforward.models.RequestTag;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.List;

public class ManageRequestTag {


    public static void main(String[] args) {

        ManageRequestTag mt = new ManageRequestTag();
    }

    public ManageRequestTag(){

    }

    /**
     * Creates a new tag.
     * @return
     */
    public RequestTag createRequestTag(String tagname) {

        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        RequestTag tag;

        try
        {
            t = session.beginTransaction();

            tag = new RequestTag(tagname);
            session.save(tag);
            session.flush();
            t.commit();
        } catch (Exception e)
        {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return null;
        } finally
        {
            session.close();
        }

        System.out.println("successfully added tag");
        return tag;
    }

    /**
     * Updates the tag with the new information provided.
     * @return
     */
    public RequestTag updateTag(int tid, String tagname) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery("update Tag set tagname = :val where tid = :id");
        query.setParameter("val", tagname);
        query.setParameter("id", tid);

        RequestTag t = new RequestTag(tagname);
        t.setRequestTid(tid);

        boolean tagUpdated = makeQuery(session, query);
        if (tagUpdated) {
            return t;
        }
        else {
            return null;
        }
    }

    /**
     * Deletes the tag
     * @return true if the tag was successfully removed.
     */
    public boolean deleteRequestTag(int tid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery("delete RequestTag where tid = :id");
        query.setParameter("id", tid);

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

    /**
     * Gets all possible tags from the DB
     * @return a list of tags
     */
    public List<RequestTag> getAllRequestTags() {

        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        List<RequestTag> r = null;

        try
        {
            t = session.beginTransaction();

            String s = "from RequestTag";
            r = (List<RequestTag>) session.createQuery(s).list();

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
            return r;
        }
    }
}
