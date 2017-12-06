package giveitforwardtests;

import giveitforward.models.User;
import giveitforward.models.UserRequestPair;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class TestUserRequestPair {


    public static void main(String[] args) {

        Session session = SetupTests.getFactory().openSession();
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
