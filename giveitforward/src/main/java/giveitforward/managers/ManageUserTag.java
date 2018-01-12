package giveitforward.managers;


import giveitforward.models.UserTag;
import org.hibernate.SessionFactory;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.*;

public class ManageUserTag {

    private static SessionFactory factory;

    public static void main(String[] args) {

        ManageUserTag mt = new ManageUserTag();

        List<String> l = mt.getAllTagsByUID(2);
    }

    public ManageUserTag(){
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
    public UserTag createTag(String tagname) {

        Session session = factory.openSession();
        Transaction t = null;
        UserTag tag;

        try
        {
            t = session.beginTransaction();

            tag = new UserTag(tagname);
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
            factory.close();
        }

        System.out.println("successfully added tag");
        return tag;
    }

    /**
     * Updates the tag with the new information provided.
     * @return
     */
    public UserTag updateTag(int tid, String tagname) {
        Session session = factory.openSession();
        Query query = session.createQuery("update Tag set tagname = :val where tid = :id");
        query.setParameter("val", tagname);
        query.setParameter("id", tid);

        UserTag t = new UserTag(tagname);
        t.setUserTid(tid);

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
    public boolean deleteTag(int tid) {
        Session session = factory.openSession();
        Query query = session.createQuery("delete Tag where tid = :id");
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
            factory.close();
        }
    }

    /**
     * Gets all possible tags from the DB
     * @return a list of tags
     */
    public List<UserTag> getAllTags() {

        Session session = factory.openSession();
        Transaction t = null;
        List<UserTag> r = null;

        try
        {
            t = session.beginTransaction();

            String s = "from Tag";
            r = (List<UserTag>) session.createQuery(s).list();

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
     * Gets all tags associated with a certain user
     * @param uid the uid of the specific user
     * @return a list of tags
     */
    public List<String> getAllTagsByUID(int uid) {
        Session session = factory.openSession();
        Transaction t = null;
        List<String> allTags = null;

        try
        {
            t = session.beginTransaction();

            String q = "select t.tagname from UserTagPair as p join p.id.tagid as t where p.id.userid = :id";
            allTags = session.createQuery(q).setInteger("id", uid).list();

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
            return allTags;
        }
    }
}
