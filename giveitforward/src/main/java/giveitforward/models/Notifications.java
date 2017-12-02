package giveitforward.models;

import javax.persistence.*;

@Entity
@Table(name = "notifications") //TODO: does case matter?
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid", unique = true, nullable = false)
    private int nid;

    //TODO: I don't think we need this...
    @Column(name = "date")
    private String date;

    @Column(name = "message")
    private String message;

    @Id //TODO: Does this need to be an Id?
    @Column(name = "uid")
    private int uid;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
