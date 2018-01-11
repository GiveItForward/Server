package giveitforward.models;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    private int tid;

    @Column(name = "tagname")
    private String tagname;

    public Tag() {}

    public Tag(String _tagname) {
        tagname = _tagname;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTagame() {
        return tagname;
    }

    public void setTag(String tagname) {
        this.tagname = tagname;
    }
}