package giveitforward.models;

import org.json.JSONArray;
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

    public String asString() {
        return null;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("nid", this.nid);
        object.put("date", this.date);
        object.put("message", this.message);
        object.put("uid", this.message);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        return false;
    }
}
