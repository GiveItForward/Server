package giveitforward.models;
import javax.persistence.*;

@Entity
@Table(name = "thank_yous")
public class ThankYous {

    @Id
    @Column(name = "rid")
    private Integer rid;

    @Column(name = "note")
    private String note;

    @Column(name = "image")
    private String image;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
