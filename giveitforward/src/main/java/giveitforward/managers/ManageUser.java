package giveitforward.managers;

import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;

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
        mu.loginUser("boo@email.com", "pswd");
    }

    public ManageUser() {
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public User getUserFromEmail(String email)
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

    public User loginUser(String email, String password)
    {
        User u = getUserFromEmail(email);

        if (u == null)
        {
            System.out.println("USER DOESN'T EXIST");
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
        return u;
    }

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

    public boolean deactivateUser(String email)
    {
        User u = getUserFromEmail(email);

        //u.setInactivedatedate();
        return false;
    }
}
