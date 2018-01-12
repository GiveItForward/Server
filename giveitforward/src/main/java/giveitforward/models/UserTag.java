package giveitforward.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tag")
public class UserTag {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    private int tid;

    @Column(name = "tagname")
    private String tagname;

    @Column(name = "tag")
    private String tag;

    public UserTag(String tagname) {
        this.tagname = tagname;
    }

    public UserTag() { }

    public int getUserTid() {
        return tid;
    }

    public void setUserTid(int tid) {
        this.tid = tid;
    }

    public String getUserTagame() {
        return tagname;
    }

    public void setUserTag(String tagname) {
        this.tagname = tagname;
    }

    public String getUserTagname() {
        return tagname;
    }

    public void setUserTagname(String tagname) {
        this.tagname = tagname;
    }
}