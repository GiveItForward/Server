package giveitforward.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@Table(name = "user", schema = "postgres")
@Table(name = "users")
public class User extends Model {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uid_generator")
    @SequenceGenerator(name = "uid_generator", sequenceName = "users_uid_seq")
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
    private String image;

    @Column(name = "bio")
    private String bio;

    @Column(name = "signupdate")
    private Timestamp signupdate;

    @Column(name = "inactivedate")
    private Timestamp inactivedate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_tag_pair",
            joinColumns = { @JoinColumn(name = "userid") },
            inverseJoinColumns = { @JoinColumn(name = "tagid") }
    )
    private Set<UserTag> tags = new HashSet<UserTag>();

    public User() {

    }

    public User(Integer uid, String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orgId = orgId;
        this.image = photo;
        this.bio = bio;
    }

    public User(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio, Timestamp signupdate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orgId = orgId;
        this.image = photo;
        this.bio = bio;
        this.signupdate = signupdate;
    }

    public User(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.image = photo;
        this.bio = bio;
    }

    public Set<UserTag> getTags() {
        return tags;
    }

    public void setTags(Set<UserTag> tags) {
        this.tags = tags;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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
        return image;
    }

    public void setPhoto(String photo) {
        this.image = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Timestamp getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Timestamp signupdate) {
        this.signupdate = signupdate;
    }

    public Timestamp getInactivedatedate() {
        return inactivedate;
    }

    public void setInactivedatedate(Timestamp inactivedate) {
        this.inactivedate = inactivedate;
    }

    public String asString() {
        String tag_display = "";
        if(tags.isEmpty() == false) {
            for (UserTag tag : tags) {
                tag_display += tag.asString() + "\n";
            }
            return "name: " + username + "\nemail: " + email + "\ntags:\n" + tag_display;
        }
        return "name: " + username + "\nemail: " + email;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("uid", this.uid);
        object.put("email", this.email);
        object.put("username", this.username);
        object.put("isAdmin", this.isAdmin);
        object.put("orgId", this.orgId);
        object.put("image", this.image);
        object.put("bio", this.bio);
        JSONArray arr = new JSONArray();
        for(UserTag tag: tags){
            arr.put(tag.asJSON());
        }
        object.put("tags", arr);

        // TODO - until tags are implemented send through fake placeholders for jen
//
//
//        UserTag tag1 = new UserTag();
//        tag1.setUserTid(12);
//        tag1.setUserTag("other");
//
//        UserTag tag2 = new UserTag();
//        tag2.setUserTid(12);
//        tag2.setUserTag("other");
//
//        arr.put(tag1.asJSON());
//        arr.put(tag2.asJSON());
//        object.put("tags", arr);
        return object;
    }

    public boolean populateFromJSON(JSONObject object) {
        try{
            this.uid = object.getInt("uid");
            this.email = object.getString("email");
            this.username = object.getString("username");
            this.password = object.getString("password");
//            this.image = object.getString("image");
            this.bio = object.getString("bio");
        } catch(JSONException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean populateSignupUserFromJSON(JSONObject object){
        try{
            this.email = object.getString("email");
            this.username = object.getString("username");
            this.password = object.getString("password");
            //this.isAdmin = object.getBoolean("isAdmin");
            //this.orgId = object.getInt("orgId");
//            this.image = object.getString("image");
            this.bio = object.getString("bio");
        } catch(JSONException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}