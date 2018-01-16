package giveitforward.managers;

import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import java.util.List;

public class ManageUser {
    private static SessionFactory factory;

    public static void main(String[] args) {
        String email = "single_mama@email.com";
        String username = "single_mama";
        String password = "kids_name";
        boolean isAdmin = false;
        Integer orgId = null;
        String photo = null;
        String bio = "whats up";

        ManageUser mu = new ManageUser();

        //mu.signupUser(email, username, password, isAdmin, orgId, photo, bio);
        //mu.loginUser("boo@email.com", "pswd");
        mu.deactivateUser("boo@email.com");
    }

    public ManageUser() {
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private User getUserFromEmail(String email)
    {
        Session session = factory.openSession();
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
            factory.close();
            return u;
        }
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

    /**
     * Attempts to add a new user to the DB with the provided information
     * @param email     user's email (must be unique!)
     * @param username  user's username
     * @param password  user's password
     * @param isAdmin   boolean whether the user is an admin or not
     * @param orgId     orgID of associated organization is the user is signing up as an org
     * @param photo     optional profile picture
     * @param bio       short string of information about the user
     * @return  The user object after being successfully signed up OR null if sign up was unsuccessful
     */
    public User signupUser(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio)
    {
        Session session = factory.openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            u = new User(email, username, password, isAdmin, orgId, photo, bio, new Timestamp(System.currentTimeMillis()));
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
            factory.close();
        }

        System.out.println("successfully added user");
        return u;
    }

    public User signupUser(User newUser)
    {
        Session session = factory.openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            u = new User(newUser.getEmail(), newUser.getUsername(), newUser.getPassword(), newUser.getIsAdmin(),
                    newUser.getOrgId(), newUser.getPhoto(), newUser.getBio(), new Timestamp(System.currentTimeMillis()));
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
            factory.close();
        }

        System.out.println("successfully added user");
        return u;
    }

    /**
     *  Sets the user's deactivation time to the current time - signifying that this user is deactivated
     * @param email user's email to be deactivated
     * @return  the updated user object
     */
    public User deactivateUser(String email)
    {
        User u = getUserFromEmail(email);

        u.setInactivedatedate(new Timestamp(System.currentTimeMillis()));

        Session session = factory.openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();

            session.update(u);
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

        System.out.println("successfully deactivated user");
        return u;
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
        Session session = factory.openSession();
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
            factory.close();
            return r;
        }
    }


}
