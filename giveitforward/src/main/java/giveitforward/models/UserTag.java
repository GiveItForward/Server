package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_tag")
public class UserTag extends Model{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    private int tid;

    @Column(name = "tagname")
    private String tagname;

    public UserTag(String tagname) {
        this.tagname = tagname;
    }

    public UserTag() { }

    public int getUserTid() {
        return tid;
    }

    public void setUserTid(int tid) {
        this.tid = tid;
    }

    public void setUserTag(String tagname) {
        this.tagname = tagname;
    }

    public String getUserTagname() {
        return tagname;
    }

    public void setUserTagname(String tagname) {
        this.tagname = tagname;
    }

    public String asString() {

        return "tagname: " + tagname;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("tid", this.tid);
        object.put("tagname", this.tagname);
        object.put("verified", false);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        return false;
    }
}