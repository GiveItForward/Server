package giveitforward.managers;


import giveitforward.models.Organization;
import giveitforward.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.List;

public class ManageOrganization
{

    /**
     * Used for quick testing.
     */
    public static void main(String[] args)
    {
        ManageOrganization manager = new ManageOrganization();
        //manager.approveOrganization(1);
        Organization org = manager.createOrganization(new Organization("F", "F", "F", "F", "F", "F", "F"), 2);
        //Organization org = manager.approveOrganization(7);
        //System.out.println(org.asJSON().toString());
        //Approved orgs
//        List<Organization> orgs = manager.getAllOrgs();
//        for(Organization o : orgs){
//
//            System.out.println(o.asString());
//            System.out.println(o.asJSON());
//        }

        //Pending orgs
//        List<Organization> orgs2 = manager.getAllPendingOrgs();
//        for(Organization o : orgs2){
//
//            System.out.println(o.asString());
//            System.out.println(o.asJSON());
//        }
    }

    public ManageOrganization()
    {
    }

    /**
     * Creates an organization with a null timestamp to represent that this organization is pending.
     * @param name org name to be displayed.
     * @param email org email to be displayed
     * @param website org website to be displayed.
     * @param phone_number
     * @return organization which was added to the organization table.
     */
    public Organization createOrganization(Organization org, int uid)
    {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try
        {
            t = session.beginTransaction();
            session.save(org);
            session.flush();
            t.commit();
            t = session.beginTransaction();
            ManageUser mu = new ManageUser();
            mu.addOrgToUser(uid, org.getOid());
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
        }

        System.out.println("successfully added organization");
        return org;
    }

    /**
     * Update organization with changed values in Org Obj.
     * @param org - the org to be chaanged with its updated values
     * @return -- the updated org obj - or null for failure.
     */
    public Organization updateOrganization(Organization org)
    {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        Organization oldOrg = getOrgByOrgId(org.getOid());
        org.setApproveddate(oldOrg.getApproveddate());
        org.setInactivedate(oldOrg.getInactivedate());
        try {
            t = session.beginTransaction();
            session.update(org);
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
        System.out.println("successfully updated organization");
        return org;
    }

    /**
     * Adds a timestamp to the organization to mark that it has been approved.
     * @param oid
     * @return the org that has been approved.
     */
    public Organization approveOrganization(Organization org)
    {
        Organization oldOrg = getOrgByOrgId(org.getOid());
        org.setInactivedate(oldOrg.getInactivedate());
        Session s = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        try {
            t = s.beginTransaction();
            org.setApproveddate(new Timestamp(System.currentTimeMillis()));
            s.update(org);
            t.commit();
        } catch (Exception e) {
            return null;
        } finally {
            s.close();
        }
        return org;
    }

    /**
     * Removes an organization from the organization table by setting its inactivedate
     * @return true if the organization was successfully removed.
     */
    public Organization deleteOrganization(Organization org)
    {
        Organization oldOrg = getOrgByOrgId(org.getOid());
        org.setApproveddate(oldOrg.getApproveddate());
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();
            org.setInactivedate(new Timestamp(System.currentTimeMillis()));
            session.update(org);
            session.flush();
            t.commit();
        } catch (Exception e) {
            if (t != null)
            {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }

        System.out.println("successfully deactivated org");
        return org;
    }

    /**
     * @return a list of all pending organizations.
     */
    public List<Organization> getAllPendingOrgs()
    {
        return makeQuery("from Organization where approveddate is null and inactivedate is null");
    }

    /**
     * @return a list of all non-pending organizations.
     */
    public List<Organization> getAllOrgs()
    {

        return makeQuery("from Organization where approveddate is not null and inactivedate is null");
    }

    public Organization getOrgByOrgId(int oid){
        String query = "select o from Organization o where oid = " + oid;
        List<Organization> org = makeQuery(query);

        if(org.size() != 1){
            return null;
        }
        return org.get(0);
    }


    /********************************** Queries *********************************/

    /**
     * @param query HQL query to be performed.
     * @return a list of Requests which results from the given query.
     */
    private List<Organization> makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();
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
            return orgs;
        }
    }
}
