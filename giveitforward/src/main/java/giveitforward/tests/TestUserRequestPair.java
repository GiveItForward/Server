package giveitforward.tests;

import giveitforward.models.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

public class TestUserRequestPair {

    private static SessionFactory factory;
    private static AnnotationConfiguration config;

    public static void main(String[] args) {

        try {
            config = new AnnotationConfiguration().configure();
            factory = config.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        Session session = factory.openSession();
        Transaction t = null;
        try {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("uid", new Integer(1)));

            User u1 = (User) criteria.uniqueResult();


            UserRequestPair ur1 = new UserRequestPair();
            ur1.setId(u1.getUid(), 3);

            session.save(ur1);
            session.flush();
            t.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
        }
        finally {
            session.close();
        }

//        System.out.println("successfully saved");
    }
}
