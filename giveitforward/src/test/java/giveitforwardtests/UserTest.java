package giveitforwardtests;

import giveitforward.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import org.junit.Test;

public class UserTest {

    private static Session session;
    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        sessionFactory.close();
    }


    @Test
    public void loginUser() throws Exception {
        session.beginTransaction();
        session.save( new User("cupcake@gmail.com", "cupcake", "pwd", false, 0, null, null) );
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void getUid() throws Exception {
    }

    @Test
    public void setUid() throws Exception {
    }

    @Test
    public void getEmail() throws Exception {
    }

    @Test
    public void setEmail() throws Exception {
    }

    @Test
    public void getPassword() throws Exception {
    }

    @Test
    public void setPassword() throws Exception {
    }

    @Test
    public void getIsAdmin() throws Exception {
    }

    @Test
    public void setIsadmin() throws Exception {
    }

    @Test
    public void getOid() throws Exception {
    }

    @Test
    public void setOid() throws Exception {
    }

    @Test
    public void getPhoto() throws Exception {
    }

    @Test
    public void setPhoto() throws Exception {
    }

    @Test
    public void getBio() throws Exception {
    }

    @Test
    public void setBio() throws Exception {
    }

}