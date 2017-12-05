package giveitforward.managers;

import giveitforwardtests.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

public class ManageUser {
    private static SessionFactory factory;

    public static void main(String[] args) {
        String email = "boo@email.com";
        String password = "pswd";

        ManageUser mu = new ManageUser();

        mu.loginUser(email, password);
    }
    public ManageUser(){
        try {
            factory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public boolean loginUser(String email, String password) {
        boolean result = false;
        Session session = factory.openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            User u = (User) criteria.uniqueResult();

            if (u == null) {
                System.out.println("USER DOESN'T EXIST");
            }
            else {
                System.out.println("Email: " + u.getEmail());
                String pword = u.getPassword();
                System.out.println("Password: " + pword);

                if (pword.equals(password)) {
                    System.out.println("Logged in!");
                    result = true;
                }
                else {
                    System.out.println("Passwords don't match!");
                }
            }
            t.commit();
        }
        catch (Exception e){
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        }
        finally {
            session.close();
            return result;
        }
    }
}
