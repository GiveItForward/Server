package giveitforwardtests;

import giveitforward.models.UserTag;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class Test {
    public static void main(String[] args) {
        AnnotationConfiguration blah = new AnnotationConfiguration().configure();
        Session session = blah.buildSessionFactory().openSession();
        Transaction t = null;
        try {
            t = session.beginTransaction();
            UserTag t1 = new UserTag("yay");
            session.save(t1);
            session.flush();
            t.commit();
        }
        catch (Exception e){
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
        }
        finally {
            session.close();
        }

        System.out.println("successfully saved");
    }
}
