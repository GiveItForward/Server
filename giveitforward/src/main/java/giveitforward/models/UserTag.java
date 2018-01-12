package giveitforward.models;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserTagPair> userpairs;

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

    public List<UserTagPair> getUserpairs() {
        return userpairs;
    }

    public void setUserpairs(List<UserTagPair> userpairs) {
        this.userpairs = userpairs;
    }
}