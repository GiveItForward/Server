package giveitforward.managers;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import giveitforward.gateway.GIFOptional;
import giveitforward.models.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageUser {

    public static void main(String[] args) {
        String email = "DELETE ME";
        String username = "DELETE";
        String password = "DELETE";
        boolean isAdmin = false;
        Integer orgId = null;
        String photo = null;
        String bio = "DELETE";
        String first = "DELETE";
        String last = "DELETE";

        ManageUser mu = new ManageUser();
//        User u = mu.getUserfromUID(1);
//        System.err.println(u.asJSON());
//        mu.updateUser(new User(1,"boo@email.com", "boo", "b7fb0394c7183fd5cac17fb41961c826212a185070e4c1d2f4920e51c1dee35f",
//                false, null, "/img/glasses_profile_pic.png", "updated bio"));

        //mu.signupUser(new User(email, username, password, isAdmin, orgId, photo, bio, first, last));
        //mu.loginUser("boo@email.com", "b7fb0394c7183fd5cac17fb41961c826212a185070e4c1d2f4920e51c1dee35f");
//        mu.deactivateUser("boo@email.com");
//
//        for(User u : mu.getAllUsers()){
//            System.err.println(u.asJSON());
//        }

//        User u = mu.getUserfromUID(3350);
//        u.setOrgId(1);
//        mu.promoteUserOrg(u);
//        System.err.println(u.asJSON());

//        User ur = mu.getUserfromUID(4350);
//        ur.setOrgId(1);
//        mu.promoteUserOrg(ur);
//		mu.demoteUserOrg(ur);
//        mu.deactivateUser(ur);

        //List<User> u = mu.searchForUser("sara");
        //mu.verifyTag(3959, 3, 2);
        //mu.promoteUserAdmin(mu.getUserfromUID(4350));

//        User u = mu.updatePic("1", "/img/profile/grey_default.png");
    }

    public ManageUser() {
    }


    public User getUserFromEmail(String email) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            u = (User) criteria.uniqueResult();

            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally {
            session.close();
            return u;
        }
    }

    public User verifyTag(int uid, int oid, int tid) {
        User currentuser = this.getUserFromUid(uid);
        String query = "update user_tag_pair set verified_by = " + oid + " where userid = " + uid + " and tagid = " + tid;
        String noteMessage = "Your TAGS were VERIFIED by ";
        int note_type = 3;
        for (UserTagPair t : currentuser.getTags()) {
//    		if (t.getTid() == tid && t.getVerifiedBy() == oid){
//    			// unverify tag only if this org verified it in the first place
//			}
            if (t.getTid() == tid && t.getVerifiedBy() != null) {
                // unverify tag no matter which org verified it.
                query = "update user_tag_pair set verified_by = NULL where userid = " + uid + " and tagid = " + tid;
                noteMessage = "Your TAGS were UNVERIFIED by ";
                note_type = 4;
                break;
            }
        }

        boolean status = makeSQLQuery(query);

        if (status == false) {
            return null;
        }

        // Notification side effect
        ManageOrganization orgManager = new ManageOrganization();
        Organization o = orgManager.getOrgByOrgId(oid);
        if (o != null) {
            String orgname = o.getName();
            ManageNotification noteManager = new ManageNotification();
            noteManager.createNotification(noteMessage + orgname, uid, note_type, null);
        }

        return getUserfromUID(uid);
    }

    private User getUserFromUid(int uid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("uid", uid));

            u = (User) criteria.uniqueResult();

            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally {
            session.close();
            return u;
        }
    }


    public boolean deleteUser(User user) {
        //Delete all tags
        ManageUserTag mut = new ManageUserTag();
        for (UserTagPair utp : user.getTags()) {
            mut.deleteTag(utp.getTid());
        }

        //Delete any existing email codes.
        EmailCode ec = ManageEmail.getEmailCodeFromUser(user, 'c');
        if (ec != null) {
            ManageEmail.deleteHash(ec);
        }

        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

            session.delete(user);
            session.flush();
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

        System.out.println("successfully deleted user");
        return true;
    }

    /**
     * Retrieves a user from the DB given an email and verifies the password if the user exists
     *
     * @param email    user email attempting to log in
     * @param password user password attempting to be verified
     * @return The user object with all of their pertinent information OR null if the login was unsuccessful
     */
    public GIFOptional loginUser(String email, String password) {
        GIFOptional result = new GIFOptional();
        String errMsg;

        User u = getUserFromEmail(email);

        if (u == null) {
            errMsg = "User doesn't exist";
            System.out.println(errMsg);
            result.setErrorMessage(errMsg);
        } else if (u.getInactivedate() != null) {
            errMsg = "User has been deactivated";
            System.out.println(errMsg);
            result.setErrorMessage(errMsg);
        } else if (u.getSignupdate() == null) {
            errMsg = "Unconfirmed email";
            result.setErrorMessage(errMsg);
        } else {
            System.out.println("Email: " + u.getEmail());
            String pword = u.getPassword();
            System.out.println("Password: " + pword);

            if (pword.equals(password)) {
                System.out.println("Logged in!");
                result.setObject(u);
            } else {
                errMsg = "Passwords don't match.";
                System.out.println(errMsg);
                result.setErrorMessage(errMsg);
            }
        }

        return result;
    }

    /**
     * Authenticates user through google given their unique token
     * @param token - user's token used to authenticate
     * @return either error result or User obj on success
     */
    public GIFOptional loginGoogleUser(String token) {
        GIFOptional result = new GIFOptional();
        String errMsg;

        String clientID = "516734440147-59sb0ckq0i826f0s3bquv96v858v86m3.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientID)).build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

                // Check email with our DB
                User u = getUserFromEmail(email);

                if (u == null) {
                    errMsg = "User doesn't exist";
                    System.out.println(errMsg);
                    result.setErrorMessage(errMsg);
                }
                else {
                    System.out.println("Logged in!");
                    result.setObject(u);
                }
            } else {
                errMsg = "Invalid ID token.";
                System.out.println(errMsg);
                result.setErrorMessage(errMsg);
            }
        }
        catch(Exception e) {
            errMsg = "Failed to authenticate with google.";
            System.out.println(errMsg);
            result.setErrorMessage(errMsg);
        }

        return result;
    }

    public User signupUser(User newUser) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        if (newUser.getIsAdmin() == null) {
            newUser.setAdmin(false);
        }

        try {
            t = session.beginTransaction();

            u = new User(newUser.getEmail(), newUser.getUsername(), newUser.getPassword(), newUser.getIsAdmin(),
                    newUser.getOrgId(), newUser.getPhoto(), newUser.getBio(), null, newUser.getFirstname(), newUser.getLastname());
            session.save(u);
            session.flush();
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

        System.out.println("successfully added user");
        return u;
    }

    /**
     * Sets the Signup time in the User table to verify that a users email has been confirmed.
     *
     * @param uid
     * @return
     */
    public User confirmEmail(int uid) {
        // Get user
        User u = getUserFromUid(uid);

        if (u == null) {
            return null;
        }

        u.setSignupdate(new Timestamp(System.currentTimeMillis()));
        return updateUser(u);
    }

    /**
     * Sets the user's deactivation time to the current time - signifying that this user is deactivated
     *
     * @param user user's email to be deactivated
     * @return the updated user object
     */
    public User deactivateUser(User user) {
        user.setInactivedate(new Timestamp(System.currentTimeMillis()));

        String hqlUpdate = "update User u set u.inactivedate = current_timestamp() where u.uid = " + user.getUid();


        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try {
            t = session.beginTransaction();


            int updatedEntities = session.createQuery(hqlUpdate).executeUpdate();
            session.flush();
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

        System.out.println("successfully added user");
        return u;
    }


    /**
     * Queries the DB for the user object associated with the given username
     *
     * @param username
     * @return
     */
    public User getUserFromUsername(String username) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        User u = null;

        try {
            t = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", username));

            u = (User) criteria.uniqueResult();

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
            return u;
        }
    }

//	/**
//	 * Queries the DB for
//	 * @param email
//	 * @return
//	 */
//	public User getUserFromEmail(String email){
//		List<User> ul = makeQuery("from User where email = " + email);
//		if(ul.size() != 1){
//			return null;
//		}
//		return ul.get(0);
//	}

    public List<User> getAllUsers() {
//        return makeQuery("SELECT uid, username, email, bio, isAdmin, " +
//                "orgId, photo FROM User");
        return makeQuery("from User order by uid");
    }

    public List<User> getAllActiveUsers() {
        return makeQuery("from User where inactivedate is null order by lastname asc");
    }

    public List<User> getAllAdminUsers() {
        return makeQuery("from User where isadmin is true");
    }

    /**
     * @param query HQL query to be performed.
     * @return a list of Users which results from the given query.
     */
    public List<User> makeQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;
        List<User> r = null;

        try {
            t = session.beginTransaction();
            r = (List<User>) session.createQuery(query).list();
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
        } finally {
            session.close();
            return r;
        }
    }


    /**
     * @param query HQL query to be performed.
     * @return a list of Users which results from the given query.
     */
    private boolean makeSQLQuery(String query) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            t.commit();
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("ROLLBACK");
            e.printStackTrace();
            return false;
        } finally {
            session.close();
            return true;
        }
    }

    private User updateTags(User currentUser, User updatedUser) {
        boolean match = false;
        ArrayList<UserTagPair> toBeRemoved = new ArrayList();
        ArrayList<UserTagPair> toBeAdded = new ArrayList();

        //tags to be added
        for (UserTagPair utp : updatedUser.getTags()) {

            if (!currentUser.getTags().contains(utp)) {
                toBeAdded.add(utp);
            }
        }

        //tags to be removed
        for (UserTagPair utp : currentUser.getTags()) {

            if (!updatedUser.getTags().contains(utp)) {
                toBeRemoved.add(utp);
            }
        }

        ManageUserTag mut = new ManageUserTag();
        //remove the tags.
        for (UserTagPair utp : toBeRemoved) {
            mut.ModifyUserTagPair('D', utp);
        }

        //add the tags.
        for (UserTagPair utp : toBeAdded) {
            mut.ModifyUserTagPair('A', utp);
        }

        return getUserfromUID(currentUser.getUid());
    }

    /**
     * Saves the updated user in the db.
     *
     * @param updatedUser
     * @return
     */
    public User updateUser(User updatedUser) {
        //Get all data from the current user that may not have come across in the json.
        User currentUser = getUserfromUID(updatedUser.getUid());
        if (currentUser == null) {
            return null;
        } else {
            if (updatedUser.getSignupdate() == null) {
                updatedUser.setSignupdate(currentUser.getSignupdate());
            }
            if (updatedUser.isAdmin() == null) {
                updatedUser.setAdmin(currentUser.isAdmin());
            }
            if (updatedUser.getOrgId() == null) {
                updatedUser.setOrgId(currentUser.getOrgId());
            }
            if (updatedUser.getPassword() == null) {
                updatedUser.setPassword(currentUser.getPassword());
            } else if (updatedUser.getPassword().equals(currentUser.getPassword()) == false) {
                //Double hash the password.
                String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(updatedUser.getPassword() + "supercalifragilisticexpialidocious");
                updatedUser.setPassword(sha256hex);
            }
            // Update tags.
            if (!currentUser.getTags().equals(updatedUser.getTags())) {
                currentUser = updateTags(currentUser, updatedUser);
            }
            updatedUser.setTags(currentUser.getTags());
        }

        //Now try to freaking update. :(
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

            session.update(updatedUser);

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

        System.out.println("successfully updated user");
        return updatedUser;

    }


    /**
     * Updates the given user with the updated fields in the user object
     * @param user - the updated user
     * @return - true if the user was successfully updated
     */
//        public boolean updateUser(User user) {
//            if (updateQuery(user) == null) {
//				return false;
//			}
//			else {
//            	return true;
//			}
//        }

    /**
     * Get a user object from DB matching on UID
     *
     * @param uid - uid of desired user
     * @return - the user with that uid
     */
    public User getUserfromUID(int uid) {
        return makeQuery("from User where uid = " + uid).get(0);
    }

    public boolean promoteUserOrg(User newUser) {
        String query = "update users set oid=" + newUser.getOrgId() + " where uid=" + newUser.getUid();
        return makeSQLQuery(query);
    }

    public boolean promoteUserAdmin(User newUser) {
        String query = "update users set isAdmin=true where uid=" + newUser.getUid();
        if (makeSQLQuery(query)) {
            ManageNotification noteManager = new ManageNotification();
            noteManager.createNotification("You have been promoted to an admin.", newUser.getUid(), 9, null);
            return true;
        }
        return false;
    }

    public boolean demoteUserOrg(User newUser) {
        String query = "update users set oid=NULL where uid=" + newUser.getUid();
        return makeSQLQuery(query);
    }

    public boolean demoteUserAdmin(User newUser) {
        String query = "update users set isAdmin=false where uid=" + newUser.getUid();
        return makeSQLQuery(query);
    }

    public User addOrgToUser(int uid, int oid) {
        User updatedUser = getUserfromUID(uid);
        updatedUser.setOrgId(oid);
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();

            session.update(updatedUser);

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

        System.out.println("successfully promoted user to org user");
        return updatedUser;
    }

    /**
     * Fuzzy search for a user on a string to match with.
     * Searches based on username, email, first name, and last name
     *
     * @param match - string to match on
     * @return - list of users that match.
     */
    public List<User> searchForUser(String match) {
        match = "'%" + match + "%'";
        return makeQuery("from User where username like " + match + " or email like " + match +
                " or firstname like " + match + " or lastname like " + match + " and inactivedate is null");
    }

    /**
     * Updates a user's profile picture
     * @param uid - uid of the given user
     * @param filename - filename of the new image for that user
     * @return - the updated user object
     */
    public User updatePic(String uid, String filename) {
        String query = "update users set photo = '" + filename + "' where uid = " + uid;
        if (!makeSQLQuery(query)) {
            return null;
        } else {
            return getUserfromUID(Integer.parseInt(uid));
        }
    }
}