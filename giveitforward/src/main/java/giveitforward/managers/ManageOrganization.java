package giveitforward.managers;


import giveitforward.models.Organization;
import giveitforward.models.Request;
import giveitforward.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManageOrganization
{
    private static SessionFactory factory;

    /**
     * Used for quick testing.
     */
    public static void main(String[] args)
    {
        ManageOrganization manager = new ManageOrganization();


//        //Approved orgs
//        List<Organization> orgs = manager.getAllOrgs();
//        for(Organization o : orgs){
//
//            System.out.println(o.asString());
//        }

        //Pending orgs
        List<Organization> orgs2 = manager.getAllPendingOrgs();
        for(Organization o : orgs2){

            System.out.println(o.asString());
        }

    }

    public ManageOrganization()
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

    /**
     * Creates an organization with a null timestamp to represent that this organization is pending.
     * @param name org name to be displayed.
     * @param email org email to be displayed
     * @param website org website to be displayed.
     * @param phone_number
     * @return organization which was added to the organization table.
     */
    public Organization createOrganization(String name, String email, String website, String phone_number)
    {
        Session session = factory.openSession();
        Transaction t = null;
        Organization org = null;

        try
        {
            t = session.beginTransaction();
            org = new Organization(name, email, website, phone_number);
            session.save(org);
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
            return org;
        } finally
        {
            session.close();
            factory.close();
        }

        System.out.println("successfully added organization");
        return org;
    }

    public Organization updateOrganization(int oid)
    {
        //TODO: implement
        return null;
    }

    /**
     * Adds a timestamp to the organization to mark that it has been approved.
     * @param oid
     * @return the org that has been approved.
     */
    public Organization approveOrganization(int oid)
    {
        //TODO: implement.
        return null;
    }

    /**
     * Removes an organization from the organization table.
     * @return true if the organization was successfully removed.
     */
    public boolean deleterganization(int oid)
    {
        //TODO: implement.
        return false;
    }

    /**
     * @return a list of all pending organizations.
     */
    public List<Organization> getAllPendingOrgs()
    {

        return makeQuery("from Organization where approveddate is null");
    }

    /**
     * @return a list of all non-pending organizations.
     */
    public List<Organization> getAllOrgs()
    {

        return makeQuery("from Organization where approveddate is not null");
    }


    /********************************** Queries *********************************/

    /**
     * @param query HQL query to be performed.
     * @return a list of Requests which results from the given query.
     */
    private List<Organization> makeQuery(String query) {
        Session session = factory.openSession();
        Transaction t = null;
        List<Organization> orgs = null;

        try
        {
            t = session.beginTransaction();
            orgs = (List<Organization>) session.createQuery(query).list();
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
            return orgs;
        }
    }
}
