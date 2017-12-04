package giveitforwardobjects;


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
    private int verifiedBy;


    public UidTid getId() {
        return id;
    }

    public void setId(Integer uid, Integer tid) {
        id = new UidTid();
        id.setTid(tid);
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
    class UidTid implements Serializable {
        @Column(name = "uid")
        private Integer uid;

        @Column(name = "tid")
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

        public void setTid(Integer rid) {
            this.tid = rid;
        }
    }
}


