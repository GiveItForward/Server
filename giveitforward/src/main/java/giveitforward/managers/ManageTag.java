package giveitforward.managers;

import giveitforward.models.Tag;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ManageTag {

    private static SessionFactory factory;

    public static void main(String[] args) {

        ManageTag mt = new ManageTag();

        List<Tag> l = mt.getAllTagsByUID(2);
//        List<Tag> l = mt.getAllTags();
    }

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
    public Tag createTag(String tagname) {

        Session session = factory.openSession();
        Transaction t = null;
        Tag tag;

        try
        {
            t = session.beginTransaction();

            tag = new Tag(tagname);
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
    public Tag updateTag(int tid, String tagname) {
        Session session = factory.openSession();
        Query query = session.createQuery("update Tag set tagname = :val where tid = :id");
        query.setParameter("val", tagname);
        query.setParameter("id", tid);

        Tag t = new Tag(tagname);
        t.setTid(tid);

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
     *
     * @return
     */
    public List<Tag> getAllTags() {

        Session session = factory.openSession();
        Transaction t = null;
        List<Tag> r = null;

        try
        {
            t = session.beginTransaction();

            String s = "from Tag";
            r = (List<Tag>) session.createQuery(s).list();

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
     * Gets all tags from the DB - no particular order
     * @return
     */
    public List<Tag> getAllTagsByUID(int uid) {
        Session session = factory.openSession();
        Transaction t = null;
        List<Tag> allTags = null;

        try
        {
            String q = "select t.tid, t.tagname from Tag t join UserTagPair u on t.tid = u.tid where u.uid = :id";
            Query query = session.createQuery(q);
            query.setParameter("id", uid);

            t = session.beginTransaction();

            allTags = (List<Tag>) session.createQuery(query.toString()).list();

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
