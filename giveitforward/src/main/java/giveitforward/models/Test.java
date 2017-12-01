package giveitforward.models;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class Test {
    public static void main(String[] args) {
        AnnotationConfiguration blah = new AnnotationConfiguration().configure();
        Session session = blah.buildSessionFactory().openSession();
        Transaction t = null;
        try {
            t = session.beginTransaction();
            Tag t1 = new Tag("yay");
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
