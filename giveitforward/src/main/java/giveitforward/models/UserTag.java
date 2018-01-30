package giveitforward.models;

import org.json.JSONObject;

import javax.persistence.*;


@Entity
@Table(name = "user_tag")
public class UserTag extends Model {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tid", unique = true, nullable = false)
	private Integer tid;

	@Column(name = "tagname")
	private String tagname;


	public UserTag(String tagname) {
		this.tagname = tagname;
	}

	public UserTag() {
	}

	public int getUserTid() {
		return tid;
	}

	public void setUserTid(int tid) {
		this.tid = tid;
	}

	public String getUsertagName() {
		return tagname;
	}

	public void setUsertagName(String tagname) {
		this.tagname = tagname;
	}

	public String asString() {
		return "tagname: " + tagname;
	}

	public JSONObject asJSON() {
		JSONObject object = new JSONObject();

		if (this.tid == null) {
			object.put("tid", 0);
		}
		else {
			object.put("tid", this.tid);
		}

		if (this.tagname == null) {
			object.put("tagname", "");
		}
		else {
			object.put("tagname", this.tagname);
		}

		return object;
	}

	public boolean populateFromJSON(JSONObject obj) {
		return false;
	}
}