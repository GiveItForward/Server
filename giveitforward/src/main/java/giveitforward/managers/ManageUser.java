package giveitforward.managers;

import giveitforward.models.EmailCode;
import giveitforward.models.User;
import giveitforward.models.UserTagPair;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManageUser {

    public static void main(String[] args) {
        String email = "DELETE ME";
        String username = "DELETE";
        String password = "DELETE";
        boolean isAdmin = false;
        Integer orgId = null;
        String photo = null;
        String bio = "DELETE";
        String first = "DELETE";
        String last = "DELETE";

        ManageUser mu = new ManageUser();
//        User u = mu.getUserfromUID(1);
//        System.err.println(u.asJSON());
//        mu.updateUser(new User(1,"boo@email.com", "boo", "b7fb0394c7183fd5cac17fb41961c826212a185070e4c1d2f4920e51c1dee35f",
//                false, null, "/img/glasses_profile_pic.png", "updated bio"));

        //mu.signupUser(new User(email, username, password, isAdmin, orgId, photo, bio, first, last));
        //mu.loginUser("boo@email.com", "b7fb0394c7183fd5cac17fb41961c826212a185070e4c1d2f4920e51c1dee35f");
//        mu.deactivateUser("boo@email.com");
//
//        for(User u : mu.getAllUsers()){
//            System.err.println(u.asJSON());
//        }

//        User u = mu.getUserfromUID(3350);
//        u.setOrgId(1);
//        mu.promoteUserOrg(u);
//        System.err.println(u.asJSON());

        User ur = mu.getUserfromUID(4350);
//        ur.setOrgId(1);
//        mu.promoteUserOrg(ur);
		mu.demoteUserOrg(ur);
//        mu.deactivateUser(ur);

    }

    public ManageUser() {
    }



    private User getUserFromEmail(String email)
    {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            u = (User) criteria.uniqueResult();

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
            return u;
        }
    }

    private User getUserFromUid(int uid)
    {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("uid", uid));

            u = (User) criteria.uniqueResult();

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
            return u;
        }
    }



    public boolean deleteUser(User user){
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try
		{
			t = session.beginTransaction();

			session.delete(user);
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
			return false;
		} finally
		{
			session.close();
		}

		System.out.println("successfully deleted user");
		return true;
	}

    /**
     * Retrieves a user from the DB given an email and verifies the password if the user exists
     * @param email user email attempting to log in
     * @param password  user password attempting to be verified
     * @return  The user object with all of their pertinent information OR null if the login was unsuccessful
     */
    public User loginUser(String email, String password)
    {
        User u = getUserFromEmail(email);

        if (u == null)
        {
            System.out.println("USER DOESN'T EXIST");
            return null;
        }
        else if (u.getInactivedate() != null)
        {
            System.out.println("USER HAS BEEN DEACTIVATED");
            return null;
        }
        else {
            System.out.println("Email: " + u.getEmail());
            String pword = u.getPassword();
            System.out.println("Password: " + pword);

            if (pword.equals(password)) {
                System.out.println("Logged in!");
                return u;
            } else {
                System.out.println("Passwords don't match!");
                return null;
            }
        }
    }

    public User signupUser(User newUser)
    {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            u = new User(newUser.getEmail(), newUser.getUsername(), newUser.getPassword(), newUser.getIsAdmin(),
                    newUser.getOrgId(), newUser.getPhoto(), newUser.getBio(), null, newUser.getFirstname(), newUser.getLastname());
            session.save(u);
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

        System.out.println("successfully added user");
        return u;
    }

    /**
     * Sets the Signup time in the User table to verify that a users email has been confirmed.
     * @param uid
     * @return
     */
    public User confirmEmail(int uid){
        // Get user
        User u = getUserFromUid(uid);

        if(u == null){
            return null;
        }

        u.setSignupdate(new Timestamp(System.currentTimeMillis()));
        return updateUser(u);
    }

    /**
     *  Sets the user's deactivation time to the current time - signifying that this user is deactivated
     * @param user user's email to be deactivated
     * @return  the updated user object
     */
    public User deactivateUser(User user)
    {
		user.setInactivedate(new Timestamp(System.currentTimeMillis()));

		String hqlUpdate = "update User u set u.inactivedate = current_timestamp() where u.uid = " + user.getUid();


		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;
		User u = null;

		try
		{
			t = session.beginTransaction();


			int updatedEntities = session.createQuery( hqlUpdate ).executeUpdate();
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

		System.out.println("successfully added user");
		return u;
//        return updateUser(user);

    }

    //TODO: make a soft search?

    /**
     * Queries the DB for
     * @param username
     * @return
     */
    public List<User> getUserFromUsername(String username){
        return makeQuery("from User where username = " + username);
    }

    public List<User> getAllUsers(){
//        return makeQuery("SELECT uid, username, email, bio, isAdmin, " +
//                "orgId, photo FROM User");
        return makeQuery("from User order by uid");
    }


    /**
     * @param query HQL query to be performed.
     * @return a list of Users which results from the given query.
     */
    private List<User> makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        List<User> r = null;

        try
        {
            t = session.beginTransaction();
            r = (List<User>) session.createQuery(query).list();
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


	/**
	 * @param query HQL query to be performed.
	 * @return a list of Users which results from the given query.
	 */
	private boolean makeSQLQuery(String query) {
		Session session = SessionFactorySingleton.getFactory().openSession();
		Transaction t = null;

		try
		{
			t = session.beginTransaction();
			session.createSQLQuery(query).executeUpdate();
			t.commit();
		} catch (Exception e)
		{
			if (t != null)
			{
				t.rollback();
			}
			System.out.println("ROLLBACK");
			e.printStackTrace();
			return false;
		} finally
		{
			session.close();
			return true;
		}
	}

    private User updateTags(User currentUser, User updatedUser){
		boolean match = false;
		ArrayList<UserTagPair> toBeRemoved = new ArrayList();
		ArrayList<UserTagPair> toBeAdded = new ArrayList();

		//tags to be added
		for (UserTagPair utp : updatedUser.getTags()) {

			if (!currentUser.getTags().contains(utp)) {
				toBeAdded.add(utp);
			}
		}

		//tags to be removed
		for (UserTagPair utp : currentUser.getTags()) {

			if (!updatedUser.getTags().contains(utp)) {
				toBeRemoved.add(utp);
			}
		}

		ManageUserTag mut = new ManageUserTag();
		//remove the tags.
		for (UserTagPair utp : toBeRemoved) {
			mut.ModifyUserTagPair('D', utp);
		}

		//add the tags.
		for(UserTagPair utp : toBeAdded) {
			mut.ModifyUserTagPair('A', utp);
		}

		return getUserfromUID(currentUser.getUid());
	}

	/**
	 * Saves the updated user in the db.
	 * @param updatedUser
	 * @return
	 */
    public User updateUser(User updatedUser) {
		//Get all data from the current user that may not have come across in the json.
		User currentUser = getUserfromUID(updatedUser.getUid());
		if (currentUser == null) {
			return null;
		}
		else {
			updatedUser.setSignupdate(currentUser.getSignupdate());
			if (updatedUser.isAdmin() == null) {
				updatedUser.setAdmin(currentUser.isAdmin());
			}
			if (updatedUser.getOrgId() == null) {
				updatedUser.setOrgId(currentUser.getOrgId());
			}
			if (updatedUser.getPassword() == null) {
				updatedUser.setPassword(currentUser.getPassword());
			}
			// Update tags.
			if (!currentUser.getTags().equals(updatedUser.getTags())) {
				currentUser = updateTags(currentUser, updatedUser);
			}
			updatedUser.setTags(currentUser.getTags());
		}

		//Now try to freaking update. :(
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();

            session.update(updatedUser);

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

        System.out.println("successfully updated user");
        return updatedUser;

    }


        /**
         * Updates the given user with the updated fields in the user object
         * @param user - the updated user
         * @return - true if the user was successfully updated
         */
//        public boolean updateUser(User user) {
//            if (updateQuery(user) == null) {
//				return false;
//			}
//			else {
//            	return true;
//			}
//        }

    /**
     * Get a user object from DB matching on UID
     * @param uid - uid of desired user
     * @return - the user with that uid
     */
    public User getUserfromUID(int uid) {

        return makeQuery("from User where uid = " + uid).get(0);
    }

	public boolean promoteUserOrg(User newUser) {
    	String query = "update users set oid=" + newUser.getOrgId() + " where uid=" + newUser.getUid();
		return makeSQLQuery(query);
	}

	public boolean promoteUserAdmin(User newUser) {
		String query = "update users set isAdmin=true where uid=" + newUser.getUid();
		return makeSQLQuery(query);
	}

	public boolean demoteUserOrg(User newUser) {
		String query = "update users set oid=NULL where uid=" + newUser.getUid();
		return makeSQLQuery(query);
	}

	public boolean demoteUserAdmin(User newUser) {
		String query = "update users set isAdmin=false where uid=" + newUser.getUid();
		return makeSQLQuery(query);
	}

	public User addOrgToUser(int uid, int oid) {
        User updatedUser = getUserfromUID(uid);
        updatedUser.setOrgId(oid);
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();

            session.update(updatedUser);

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

        System.out.println("successfully promoted user to org user");
        return updatedUser;
    }
}
