package giveitforward.models;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class Test {
    public static void main(String[] args) {
        AnnotationConfiguration blah = new AnnotationConfiguration().configure();
        Session session = blah.buildSessionFactory().openSession();

        Transaction t = session.beginTransaction();

        Tag t1 = new Tag();
        t1.setTid(1006);
        t1.setTag("real deal");

        session.persist(t1);

        t.commit();
        session.close();
        System.out.println("successfully saved");
    }
}
