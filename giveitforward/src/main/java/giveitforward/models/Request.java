package giveitforward.models;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "request")
public class Request {

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
    private User requestor;

    @Column(name = "duid")
    private Integer duid;

    @Column(name = "tag1")
    private Integer tag1;

    @Column(name = "tag2")
    private Integer tag2;

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
        return requestor;
    }

    public void setRequestor(User requestor)
    {
        this.requestor = requestor;
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
