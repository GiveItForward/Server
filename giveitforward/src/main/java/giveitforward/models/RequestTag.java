package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "request_tag")
public class RequestTag extends Model{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    private int tid;

    @Column(name = "tagname")
    private String tagname;

    public RequestTag(String tagname) {
        this.tagname = tagname;
    }

    public RequestTag() { }

    public int getRequestTid() {
        return tid;
    }

    public void setRequestTid(int tid) {
        this.tid = tid;
    }

    public String getRequestTagname() {
        return tagname;
    }

    public void setRequestTagname(String tagname) {
        this.tagname = tagname;
    }


    public String asString() {
        return null;
    }

    public JSONObject asJSON() {
        return null;
    }

    public boolean populateFromJSON(JSONObject obj) {
        return false;
    }
}