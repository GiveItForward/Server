package giveitforward.models;
import org.json.JSONObject;
import javax.persistence.*;
import java.sql.Timestamp;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rid")
    private ThankYou thankYou;

    @Column(name = "duid")
    private Integer duid;

    @Column(name = "tag1")
    private Integer tag1;

    @Column(name = "tag2")
    private Integer tag2;

    public Request(String description, Double amount, String image, User ruid, ThankYou thankYou, Integer duid, Integer tag1, Integer tag2) {
        this.description = description;
        this.amount = amount;
        this.image = image;
        this.ruid = ruid;
        this.thankYou = thankYou;
        this.duid = duid;
        this.tag1 = tag1;
        this.tag2 = tag2;
    }


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
        object.put("duid", this.duid);
        object.put("tag1", this.tag1);
        object.put("tag2", this.tag2);
        object.put("thankyou", this.thankYou.asJSON());
        object.put("ruid", this.ruid.asJSON());
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

    public Integer getTag1() {
        return tag1;
    }

    public void setTag1(Integer tag1) {
        this.tag1 = tag1;
    }

    public Integer getTag2() {
        return tag2;
    }

    public void setTag2(Integer tag2) {
        this.tag2 = tag2;
    }
}
