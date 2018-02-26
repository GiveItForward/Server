package giveitforward.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "notification")
public class Notification extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid", unique = true, nullable = false)
    private int nid;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "message")
    private String message;

    @Column(name = "uid")
    private int uid;

    @Column(name = "opened")
    private boolean opened;

    public Notification() {}

    public Notification(String _message, int _uid) {
        message = _message;
        uid = _uid;
        date = new Timestamp(System.currentTimeMillis());
        opened = false;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public String asString() {
        return null;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("nid", this.nid);
        object.put("date", getDisplayDate(this.date));
        object.put("message", this.message);
        object.put("uid", this.message);
        object.put("opened", this.opened);
        return object;
    }

    public String populateFromJSON(JSONObject obj) {
		String fieldName = "";
        try {
			fieldName = "nid";
            this.nid = obj.getInt("nid");

			fieldName = "message";
            this.message = obj.getString("message");

			fieldName = "uid";
            this.uid = obj.getInt("uid");

			fieldName = "opened";
            this.opened = obj.getBoolean("opened");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return "Missing non-optional field in JSON Request " + fieldName + ".";
        }
        return null;
    }
}
