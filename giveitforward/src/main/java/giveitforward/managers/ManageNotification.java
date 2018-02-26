package giveitforward.managers;

import giveitforward.models.Notification;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ManageNotification {

    public static void main(String[] args) {
        ManageNotification manager = new ManageNotification();
        List<Notification> list = manager.seenAllNotifications(1);
    }

    public ManageNotification(){
    }

    /**
     * Creates a new notification with the given message and user, current time, and default unopened.
     * @param message -- the message attached to the new notification
     * @param uid -- the user attached to this notification.
     * @return -- the new notification or null if failed
     */
    public Notification createNotification(String message, int uid) {
        Notification n = new Notification(message, uid);
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();
            session.save(n);
            t.commit();
        } catch (Exception e) {
            session.close();
            return null;
        }
        session.close();
        return n;
    }

    /**
     * Marks a request as fulfilled.
     * @param nid -- the id for the notification that has now been seen
     * @return true if the transaction was successfully completed.
     */
    public Notification seenNotification(int nid) {
        Notification n;
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t;

        try {
            t = session.beginTransaction();
            n = (Notification) session.get(Notification.class, nid);
            n.setOpened(true);
            session.update(n);
            t.commit();
        } catch (Exception e) {
            session.close();
            return null;
        }
        session.close();
        return n;
    }

    /**
     * Set all notifications for a specific user to seen
     * @param uid -- the user who has seen all notifications
     * @return -- true for success/false for failure
     */
    public List<Notification> seenAllNotifications(int uid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t;
        List<Notification> n = getAllUnreadNotifications(uid);
        if (n.isEmpty()) {
            return null;
        }

        try {
            //String query = "update Notification set opened = true where uid = " + uid;
            for (int i = 0; i < n.size(); i++) {
                t = session.beginTransaction();
                Notification note = n.get(i);
                note.setOpened(true);
                session.update(note);
                t.commit();
            }
            //session.createQuery(query);
            t = session.beginTransaction();
            String query = "from Notification where opened = false and uid = " + uid;
            n = (List<Notification>) session.createQuery(query).list();
            t.commit();
        } catch (Exception e) {
            session.close();
            return null;
        }
        session.close();
        return n;
    }

    /**
     * Updates the request with the new information provided.
     * @return
     */
    public Notification updateNotification(Notification n) {
        Session s = SessionFactorySingleton.getFactory().openSession();
        Transaction t;

        try {
            t = s.beginTransaction();
            s.update(n);
            t.commit();
        } catch (Exception e) {
            s.close();
            return null;
        }
        return n;
    }

    /**
     * Deletes the notification
     * @return true if the notification was successfully removed.
     */
    public boolean deleteNotification(Notification n) {
        Session s = SessionFactorySingleton.getFactory().openSession();
        Transaction t;

        try {
            t = s.beginTransaction();
            s.delete(n);
            t.commit();
        } catch (Exception e) {
            s.close();
            return false;
        }
        return true;
    }

    /**
     * Get all unread notifications for a specific user
     * @param uid -- uid of the user
     * @return -- list of unread notifications (empty if all read) and null for failure
     */
    public List<Notification> getAllUnreadNotifications(int uid) {
        Session session = SessionFactorySingleton.getFactory().openSession();
        Transaction t;
        List<Notification> n;

        try {
            t = session.beginTransaction();
            String query = "from Notification where opened = false and uid = " + uid;
            n = (List<Notification>) session.createQuery(query).list();
        } catch (Exception e) {
            session.close();
            return null;
        }
        session.close();
        return n;
    }
}
