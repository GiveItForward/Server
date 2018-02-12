package giveitforward.models;

import giveitforward.managers.ManageRequest;
import giveitforward.managers.ManageRequestTag;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
        return "tid: " + this.tid + "tagname: " + this.tagname + ".";
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("tid", this.tid);
        object.put("tagname", this.tagname);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        this.tagname = obj.getString("tagname");
        try{
            this.tid = obj.getInt("tid");
        }
        catch(JSONException e){
            this.tid = new ManageRequestTag().getTagByTagname(this.tagname).getRequestTid();
        }
        this.tagname = obj.getString("tagname");
        return true;
        //TODO: If something goes wrong, return false!
    }
}