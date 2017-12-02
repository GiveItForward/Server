package giveitforward.models;
import javax.persistence.*;

@Entity
@Table(name = "user_request_pairs")
public class UserRequestPairs {

    @Column(name = "uid_request")
    private String uidRequest;

    @Column(name = "rid")
    private String rid;

    @Column(name = "uid_donate")
    private String uidDonate;

    public String getUidRequest() {
        return uidRequest;
    }

    public void setUidRequest(String uidRequest) {
        this.uidRequest = uidRequest;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUidDonate() {
        return uidDonate;
    }

    public void setUidDonate(String uidDonate) {
        this.uidDonate = uidDonate;
    }
}
