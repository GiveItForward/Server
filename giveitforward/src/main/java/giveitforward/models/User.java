package giveitforward.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;


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
    private Boolean isAdmin;

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

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
//    @JoinTable(
//            name = "user_tag_pair",
//            joinColumns = { @JoinColumn(name = "userid") },
//            inverseJoinColumns = { @JoinColumn(name = "tagid") }
//    )
//    private Set<UserTag> tags = new HashSet<UserTag>();

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "userid")
    private Set<UserTagPair> tags = new HashSet<UserTagPair>();

    public User() {

    }

    public User(Integer uid, String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio, String first, String last) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orgId = orgId;
        this.image = photo;
        this.bio = bio;
        this.signupdate = null;
        this.firstname = first;
        this.lastname = last;
    }

    public User(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio, Timestamp signupdate, String firstname, String lastname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.orgId = orgId;
        this.image = photo;
        this.bio = bio;
        this.signupdate = signupdate;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String email, String username, String password, boolean isAdmin, Integer orgId, String photo, String bio, String first, String last) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.image = photo;
        this.bio = bio;
        this.signupdate = null;
        this.firstname = first;
        this.lastname = last;
    }

//    public Set<UserTag> getTags() {
//        return tags;
//    }
//
//    public void setTags(Set<UserTag> tags) {
//        this.tags = tags;
//    }

    public Set<UserTagPair> getTags() {
    return tags;
}

    public void setTags(Set<UserTagPair> tags) {
        this.tags = tags;
    }

    public Boolean isAdmin() {
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

    public Boolean getIsAdmin() {
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

    public Timestamp getInactivedate() {
        return inactivedate;
    }

    public void setInactivedate(Timestamp inactivedate) {
        this.inactivedate = inactivedate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String asString() {
        String tag_display = "";
        if(tags.isEmpty() == false) {
            for (UserTagPair tag : tags) {
                tag_display += tag.asString() + "\n";
            }
            return "name: " + username + "\nemail: " + email + "\ntags:\n" + tag_display;
        }
        return "name: " + username + "\nemail: " + email;
    }

    public JSONObject asRequestJSON(){
        JSONObject object = new JSONObject();

        if(this.uid == null){
            object.put("uid", "");
        }
        else {
            object.put("uid", this.uid);
        }

        if(this.username == null) {
            object.put("username", "");
        }
        else {
            object.put("username", this.username);
        }

        if (this.image == null) {
            object.put("image", "");
        }
        else {
            object.put("image", this.image);
        }

        JSONArray arr = new JSONArray();

        if(tags.isEmpty()){
            arr.put(new UserTagPair().asJSON());
        }
        else {
            for (UserTagPair tag : tags) {
                arr.put(tag.asJSON());
            }
        }

        object.put("tags", arr);
        return object;
    }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();

        if(this.uid == null){
			object.put("uid", "");
		}
		else {
			object.put("uid", this.uid);
		}

		if(this.email == null) {
        	object.put("email", "");
		}
		else {
			object.put("email", this.email);
		}

		if(this.username == null) {
        	object.put("username", "");
		}
		else {
			object.put("username", this.username);
		}

        object.put("isAdmin", this.isAdmin);

		if(this.orgId == null) {
        	object.put("orgId", "");
		}
		else {
			object.put("orgId", this.orgId);
		}

		if (this.image == null) {
			object.put("image", "");
		}
		else {
			object.put("image", this.image);
		}

		if(this.bio == null) {
			object.put("bio", "");
		}
		else {
			object.put("bio", this.bio);
		}

		if(this.firstname == null) {
		    object.put("firstname", "");
        }
        else {
		    object.put("firstname", this.firstname);
        }

        if(this.lastname == null) {
		    object.put("lastname", "");
        }
        else {
		    object.put("lastname", this.lastname);
        }

        JSONArray arr = new JSONArray();

        if(tags.isEmpty()){
            arr.put(new UserTagPair().asJSON());
        }
        else {
            for (UserTagPair tag : tags) {
                arr.put(tag.asJSON());
            }
        }

        object.put("tags", arr);
        return object;
    }

    public String populateFromJSON(JSONObject object) {
		String fieldName = "";
        try{
        	fieldName = "uid";
        	try {
				this.uid = object.getInt("uid");
			}
			catch(JSONException e){
        		//leave empty in case of creation.
			}

			fieldName = "email";
        	try {
				this.email = object.getString("email");
			}
			catch(JSONException e){

			}

			fieldName = "username";
        	try {
				this.username = object.getString("username");
			}
			catch(JSONException e){

			}
			fieldName = "password";
            try {
				this.password = object.getString("password");
			}
			catch(JSONException e)
			{
				//When updating, we will need to check for this value and fetch it from the DB.
				this.password = null;
			}

			fieldName = "image";
			try {
				this.image = object.getString("image");
			}
			catch(JSONException e)
			{
				this.image = null;
			}

			fieldName = "bio";
			try {
				this.bio = object.getString("bio");
			}
			catch(JSONException e){
				this.bio = null;
			}

			fieldName = "orgId";
            try {
				this.orgId = object.getInt("orgId");
			}
			catch(JSONException e){
				//When updating, we will need to check for this value and fetch it from the DB.
            	this.orgId = null;
			}

			fieldName = "isAdmin";
			try {
            	this.isAdmin = object.getBoolean("isAdmin");
			}
			catch(JSONException e){
            	//When updating, we will need to check for this value and fetch it from the DB.
            	this.isAdmin = null;
			}

			fieldName = "firstname/lastname";
			try {
                this.firstname = object.getString("firstname");
                this.lastname = object.getString("lastname");
            }
            catch (JSONException e) {
                this.firstname = "John";
                this.lastname = "Doe";
            }

        }
        catch (JSONException e) {
			e.printStackTrace();
			return "Missing non-optional field in JSON Request " + fieldName + ".";
		}
		return null;
    }

//    public boolean populateSignupUserFromJSON(JSONObject object){
//        try{
//            this.email = object.getString("email");
//            this.username = object.getString("username");
//            this.password = object.getString("password");
////            this.orgId = object.getInt("orgId");
//            this.image = object.getString("image");
//            this.bio = object.getString("bio");
//        } catch(JSONException e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    public void addTag(UserTagPair newTag) {
        if(this.tags == null){
            this.tags = new HashSet<UserTagPair>();
        }
        this.tags.add(newTag);
    }
}