package giveitforward.models;


import javax.persistence.*;

@Entity
@Table(name = "user_tag_pairs")
public class UserTagPairs {

    @Column(name = "uid", unique = true, nullable = false)
    private int uid;

    @Column(name = "tid")
    private int tid;

    @Column(name = "time_limit")
    private String time;

    @Column(name = "verified_by")
    private int verifiedBy;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
}
