package giveitforward.managers;


import giveitforward.models.User;
import giveitforward.models.UserTag;
import giveitforward.models.UserTagPair;
import org.hibernate.SessionFactory;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.*;

public class ManageUserTag {


    public static void main(String[] args) {

        ManageUserTag mt = new ManageUserTag();
//        List<UserTag> u = mt.getAllTags();
//        List<String> l = mt.getAllTagsByUID(2);

        UserTagPair utp = mt.getUserTagPair(1300,2);
        System.out.println(utp.asJSON());

        User u = new ManageUser().getUserfromUID(4101);
        UserTag t = mt.getTagByTagname("lgbt");
//        mt.UpdateTagToUser(t, u);

//        User user = new ManageUser().getUserfromUID(1);
//        mt.AddTagToUser(u.get(0), user);
    }

    public ManageUserTag(){

    }

    public UserTagPair AddTagToUser(UserTag tag, User user){
		UserTag realTag = getTagByTagname(tag.getUsertagName());
    	UserTagPair utp = new UserTagPair(user.getUid(), realTag);

		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try
		{
			t = session.beginTransaction();

			session.save(utp);
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
		return utp;
	}

	public UserTagPair getUserTagPair(int uid, int tid){
    	String query = "select t from UserTagPair t where userid = " + uid + "and tagid = " + tid;

		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;
		List<UserTagPair> utp = null;

		try
		{
			t = session.beginTransaction();
			utp = (List<UserTagPair>) session.createQuery(query).list();
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
			if(utp.size() == 1) {
				return utp.get(0);
			}
			else {
				return null;
			}
		}
	}

	public UserTagPair ModifyUserTagPair(char action, UserTagPair tag){
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try
		{
			t = session.beginTransaction();
			if(action == 'D') {
				session.delete(tag);
			}
			else if(action == 'A') {
				session.save(tag);
			}
			else {
				return null;
			}
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

//	public UserTagPair UpdateTagToUser(UserTag tag, User user){
//		UserTag realTag = getTagByTagname(tag.getUsertagName());
//		UserTagPair utp = getUserTagPair(user.getUid(), realTag.getUserTid());
//
//		Session session = SessionFactorySingleton.getFactory().openSession();
//		Transaction t = null;
//
//		try
//		{
//			t = session.beginTransaction();
//
//			session.update(utp);
//			session.flush();
//			t.commit();
//		} catch (Exception e)
//		{
//			if (t != null)
//			{
//				t.rollback();
//			}
//			System.out.println("ROLLBACK");
//			e.printStackTrace();
//			return null;
//		} finally
//		{
//			session.close();
//		}
//
//		System.out.println("successfully added tag");
//		return utp;
//	}

    /**
     * Creates a new tag.
     * @return
     */
    public UserTag createTag(String tagname) {

        Session session = SessionFactorySingleton.getFactory().openSession();
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
        }

        System.out.println("successfully added tag");
        return tag;
    }

    /**
     * Updates the tag with the new information provided.
     * @return
     */
    public UserTag updateTag(int tid, String tagname) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery("update UserTag set tagname = :val where tid = :id");
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
        Session session = SessionFactorySingleton.getFactory().openSession();
        Query query = session.createQuery("delete UserTag where tid = :id");
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
    public List<UserTag> getAllTags() {

        return makeQuery("from UserTag order by tagname ASC");
    }

    /**
     * Gets all tagnames associated with a certain user
     * @param uid the uid of the specific user
     * @return a list of tags
     */
    public List<String> getAllTagsByUID(int uid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        List<String> allTags = null;

        try
        {
            t = session.beginTransaction();

            String q = "select t.tagname from UserTagPair u, UserTag t where t.tid = u.id.tid " +
                    "and u.id.uid = :id order by t.tagname ASC";
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
            return allTags;
        }
    }


    public UserTag getTagByTagname(String tagname){
    	String query = "select u from UserTag u where u.tagname = '" + tagname + "'";
    	System.out.println(query);
    	List<UserTag> res = makeQuery(query);
    	if(res.size() != 1){
    		return null;
		}
		return res.get(0);
	}


    /**
     * @param query HQL query to be performed.
     * @return a list of Users which results from the given query.
     */
    private List<UserTag> makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        List<UserTag> ut = null;

        try
        {
            t = session.beginTransaction();
            ut = (List<UserTag>) session.createQuery(query).list();
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
            return ut;
        }
    }
}
