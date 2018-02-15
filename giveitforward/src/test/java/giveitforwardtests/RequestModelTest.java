package giveitforwardtests;

import giveitforward.models.Request;
import giveitforward.models.ThankYou;
import giveitforward.models.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

public class RequestModelTest {
    public static void main(String[] args) {

        int rid = 0;
        String description = "des";
         Double amount = 20.0;
         String image = "img";
//         Timestamp requesttime = null;
         User ruid = new User("email@email.com", "username", "password", false, null, "photo", "bio", "first", "last");
         ThankYou thankYou = new ThankYou(rid, "note", "image");
         Integer duid = 1;
         Integer tag1 = 5;
         Integer tag2 = 8;
        Request r = new Request(description, amount, image, ruid, thankYou, duid);

        System.out.println(r.asJSON());

    }
}
