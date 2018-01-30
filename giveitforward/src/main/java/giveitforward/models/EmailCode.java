package giveitforward.models;

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

    @Column(name = "type")
    private Character type;

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

