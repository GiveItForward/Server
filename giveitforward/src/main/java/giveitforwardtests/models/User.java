package giveitforwardtests.models;

import javax.persistence.*;

@Entity
//@Table(name = "user", schema = "postgres")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uid_generator")
    @SequenceGenerator(name="uid_generator", sequenceName = "users_uid_seq")
    @Column(name = "uid", unique = true, nullable = false)
    private Integer uid;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "isadmin")
    private boolean isAdmin;

    @Column(name = "oid")
    private Integer orgId;

    @Column(name = "photo")
    private String photo;

    @Column(name = "bio")
    private String bio;

    public User(){};

<<<<<<< HEAD
    public User(/*Integer uid,*/ String email, String username, String password, boolean isAdmin, Integer oid, String photo, String bio) {
        //this.uid = uid;
=======
    public User(Integer uid, String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio) {
        this.uid = uid;
>>>>>>> 278c6fdaeec45b85ead79ce797989d15aa4e5952
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orgId = orgId;
        this.photo = photo;
        this.bio = bio;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isadmin) {
        this.isAdmin = isadmin;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer oid) {
        this.orgId = oid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}