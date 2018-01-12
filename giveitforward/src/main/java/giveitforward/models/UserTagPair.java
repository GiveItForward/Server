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

    public void setId(User uid, UserTag tag) {
        id = new UidTid();
        id.setTagid(tag);
        id.setUserid(uid);
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

        @ManyToOne
        @JoinColumn(name="userid")
        private User userid;

        @ManyToOne
        @JoinColumn(name="tagid")
        private UserTag tagid;

        public UidTid() {
            tagid = new UserTag();
            userid = new User();
        }

        public User getUserid() {
            return userid;
        }

        public void setUserid(User uidRequest) {
            this.userid = uidRequest;
        }

        public UserTag getTagid() {
            return tagid;
        }

        public void setTagid(UserTag tagid) {
            this.tagid = tagid;
        }
    }
}
