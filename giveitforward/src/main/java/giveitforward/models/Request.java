package giveitforward.models;
import giveitforward.models.RequestTag;
import giveitforward.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "request")
public class Request extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid", unique = true, nullable = false)
    private int rid;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "image")
    private String image;

    @Column(name = "requesttime")
    private Timestamp requesttime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ruid")
    private User ruid;

    @Column(name="duid")
    private Integer duid;


    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(Timestamp requesttime) {
        this.requesttime = requesttime;
    }

    public String asString() { return "rid: " + this.rid + ", amount: " + this.amount + "."; }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("rid", this.rid);
        object.put("description", this.description);
        object.put("amount", this.amount);
        object.put("image", this.image);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        this.rid = obj.getInt("rid");
        this.image = obj.getString("image");
        this.amount = obj.getDouble("amount");
        this.description = obj.getString("description");
        return true;

        //TODO: If something goes wrong, return false!
    }

    public int getDuid()
    {
        return duid;
    }

    public void setDuid(int duid)
    {
        this.duid = duid;
    }

    public User getRequestor()
    {
        return ruid;
    }

    public void setRequestor(User requestor)
    {
        this.ruid = requestor;
    }
}
