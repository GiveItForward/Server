package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "organization")
public class Organization extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid", unique = true, nullable = false)
    private int oid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "approveddate")
    private Timestamp approveddate;

    @Column(name = "inactivedate")
    private Timestamp inactivedate;

    @Column(name = "description")
    private String description;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;


    public Organization(){}


    public Organization(String name, String email, String website, String phone_number)
    {
        this.name = name;
        this.email = email;
        this.website = website;
        this.phone_number = phone_number;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String isPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Timestamp getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(Timestamp approveddate) {
        this.approveddate = approveddate;
    }

    public Timestamp getInactivedate() {
        return inactivedate;
    }

    public void setInactivedate(Timestamp inactivedate) {
        this.inactivedate = inactivedate;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String asString() { return "oid: " + oid + ", name: " + name + "email: " + email; }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("oid", this.oid);
        object.put("email", this.email);
        object.put("website", this.website);
        object.put("name", this.name);
        object.put("phone", this.phone_number);
        object.put("description", this.description);
        object.put("address", this.address);
        object.put("image", this.image);
        return object;
    }

    public boolean populateFromJSON(JSONObject obj) {
        return false;
    }
}
