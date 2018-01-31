package giveitforward.managers;

import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.List;

public class ManageUser {

    public static void main(String[] args) {
        String email = "single_mama@email.com";
        String username = "single_mama";
        String password = "kids_name";
        boolean isAdmin = false;
        Integer orgId = null;
        String photo = null;
        String bio = "whats up";

        ManageUser mu = new ManageUser();
//        User u = mu.getUserfromUID(1);
//        System.err.println(u.asJSON());
//        mu.updateUser(new User(1,"boo@email.com", "boo", "b7fb0394c7183fd5cac17fb41961c826212a185070e4c1d2f4920e51c1dee35f",
//                false, null, "/img/glasses_profile_pic.png", "updated bio"));

        //mu.signupUser(email, username, password, isAdmin, orgId, photo, bio);
        //mu.loginUser("boo@email.com", "pswd");
//        mu.deactivateUser("boo@email.com");
//
        for(User u : mu.getAllUsers()){
            System.err.println(u.asJSON());
        }

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

		System.out.println("successfully added user");
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
        else if (u.getInactivedatedate() != null)
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
                    newUser.getOrgId(), newUser.getPhoto(), newUser.getBio(), null);
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
            return u;
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
        user.setInactivedatedate(new Timestamp(System.currentTimeMillis()));
        return updateUser(user);

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
	 * Saves the updated user in the db.
	 * @param updatedUser
	 * @return
	 */
    public User updateUser(User updatedUser) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();

            session.update(updatedUser);

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
}
