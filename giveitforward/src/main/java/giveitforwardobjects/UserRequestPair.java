package giveitforwardobjects;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_request_pair")
public class UserRequestPair {

    @EmbeddedId
    private UidRid id;

    @Column(name = "uid_donate")
    private Integer uidDonate;


    public Integer getUidDonate() {
        return uidDonate;
    }

    public void setUidDonate(Integer uidDonate) {
        this.uidDonate = uidDonate;
    }

    public void setId(Integer uidRequest, Integer rid){
        id = new UidRid();
        id.setRid(rid);
        id.setUidRequest(uidRequest);
    }

    public UidRid getId() {
        return id;
    }
}


@Embeddable
class UidRid implements Serializable {
    @Column(name = "uid_request")
    private Integer uidRequest;

    @Column(name = "rid")
    private Integer rid;

    public Integer getUidRequest() {
        return uidRequest;
    }

    public void setUidRequest(Integer uidRequest) {
        this.uidRequest = uidRequest;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}
