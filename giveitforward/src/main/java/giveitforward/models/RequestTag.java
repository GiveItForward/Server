package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class RequestTag extends Model{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    private int tid;

    @Column(name = "tagname")
    private String tagname;

    @Column(name = "tag")
    private String tag;

    public RequestTag(String tag) {
        this.tag = tag;
    }

    public RequestTag() { }

    public int getRequestTid() {
        return tid;
    }

    public void setRequestTid(int tid) {
        this.tid = tid;
    }

    public String getRequestTag() {
        return tag;
    }

    public void setRequestTag(String tag) {
        this.tag = tag;
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