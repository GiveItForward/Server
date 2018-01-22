package giveitforward.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Table(name = "user_tag_pair")
public class UserTagPair {

    @EmbeddedId
    private UidTid id;

    @Column(name = "time_limit")
    private Time time;

    @Column(name = "verified_by")
    private Integer verifiedBy;

    public UidTid getId() {
        return id;
    }

    public void setId(Integer uid, Integer tag) {
        id = new UidTid();
        id.setTid(tag);
        id.setUid(uid);
    }


    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    @Embeddable
    static class UidTid implements Serializable {

        @Column(name="userid")
        private Integer uid;

        @Column(name="tagid")
        private Integer tid;

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uidRequest) {
            this.uid = uidRequest;
        }

        public Integer getTid() {
            return tid;
        }

        public void setTid(Integer tagid) {
            this.tid = tagid;
        }
    }
}
