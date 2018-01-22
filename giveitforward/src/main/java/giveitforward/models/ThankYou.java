package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "thank_you")
public class ThankYou extends Model{

    @Id
    @Column(name = "rid")
    private Integer rid;

    @Column(name = "note")
    private String note;

    @Column(name = "image")
    private String image;

    @Column(name = "date")
    private Timestamp date;

    public ThankYou(int rid, String note, String image) {
        this.rid = rid;
        this.note = note;
        this.image = image;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String asString() {
        return null;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("rid", this.rid);
        object.put("note", this.note);
        object.put("image", this.image);
        object.put("date", this.date);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        return false;
    }
}
