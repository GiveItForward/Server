package giveitforward.models;

import giveitforward.managers.SessionFactorySingleton;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "email_codes")
public class EmailCode extends Model {

    @Id
    @Column(name = "uid", unique = true, nullable = false)
    private int uid;

    @Column(name = "uhash", unique = true, columnDefinition="bpchar")
    private String uHash; //sha256

	//Defines what the has was created for. "C" for email confirmation. "F" for forgot password. "D" for donation.
    @Column(name = "type")
    private Character type;

    @Column(name = "creation_time")
	private Timestamp creationTime;

    public EmailCode(int uid, String uhash, Character type) {
        this.uid = uid;
        this.uHash = uhash;
        this.type = type;
    }

    public EmailCode() {
    }


    public int getUid() {
        return uid;
    }

    public String getUhash() {
        return uHash;
    }

    public void setUhash(String uhash) {
        this.uHash = uhash;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

    public String asString() {
        return null;
    }

    public JSONObject asJSON() {
        JSONObject obj = new JSONObject();
        obj.put("hash", this.uHash);
    	return obj;
    }

    public String populateFromJSON(JSONObject obj) { return null; }

}


