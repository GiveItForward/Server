package giveitforward.managers;

import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

public class ManageUser
{
    private static SessionFactory factory;

    public static void main(String[] args)
    {
        String email = "archangelo@email.com";
        String username = "archangelo";
        String password = "east_side_gentlemen";
        boolean isAdmin = false;
        Integer orgId = null;
        String photo = null;
        String bio = "say we are homies";

        ManageUser mu = new ManageUser();

        mu.signupUser(email, username, password, isAdmin, orgId, photo, bio);
    }

    public ManageUser()
    {
        try
        {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex)
        {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public User loginUser(String email, String password)
    {
        boolean result = false;
        Session session = factory.openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            u = (User) criteria.uniqueResult();

            if (u == null)
            {
                System.out.println("USER DOESN'T EXIST");
            } else
            {
                System.out.println("Email: " + u.getEmail());
                String pword = u.getPassword();
                System.out.println("Password: " + pword);

                if (pword.equals(password))
                {
                    System.out.println("Logged in!");
                    return u;
                } else
                {
                    System.out.println("Passwords don't match!");
                }
            }
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

    public User signupUser(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio)
    {
        Session session = factory.openSession();
        Transaction t = null;
        User u = null;

        try
        {
            t = session.beginTransaction();

            u = new User(email, username, password, isAdmin, orgId, photo, bio);
            session.save(u);
//            session.flush();
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
}
