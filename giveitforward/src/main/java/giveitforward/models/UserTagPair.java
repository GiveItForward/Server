package giveitforward.models;

import giveitforward.managers.ManageOrganization;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;


@Entity
@Table(name = "user_tag_pair")
public class UserTagPair extends Model {

	@EmbeddedId
	private UidTid id;

	@Column(name = "time_limit")
	private Time time;

	@Column(name = "verified_by")
	private Integer verifiedBy;

	public UserTagPair() {

		id = new UidTid();
	}

	public UserTagPair(Integer uid, UserTag tag) {
		this.setId(uid, tag);
	}


	public UidTid getId() {
		return id;
	}

	public void setId(Integer uid, UserTag tag) {
		id = new UidTid();
		id.setTag(tag);
		id.setUid(uid);
	}

	public Integer getUid() {
		return id.uid;
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

	public String asString() {
		return null;
	}

	public JSONObject asJSON() {
		JSONObject object = new JSONObject();

		if (this.id.tag == null) {
			object.put("tagname", "");
			object.put("tid", "");
		}
		else {
			object.put("tagname", this.id.tag.getUsertagName());
			object.put("tid", this.id.tag.getUserTid());
		}

		if (this.verifiedBy == null) {
			object.put("verifiedBy", "");
		}
		else {
			Organization org = new ManageOrganization().getOrgByOrgId(this.verifiedBy);
			if(org == null){
				object.put("verifiedBy", "");
			}
			else {
				object.put("verifiedBy", org.getName());
			}
		}
		return object;
	}

	@Override
	public boolean equals(Object o){
		UserTagPair utp = (UserTagPair)o;
		return this.getTid() == utp.getTid();
	}

	@Override
	public int hashCode(){
		Integer tid = this.getTid();
		return tid.hashCode();
	}

	public String populateFromJSON(JSONObject obj) {
		String fieldName = "";
		try {
			fieldName = "verifiedBy";
			this.verifiedBy = obj.getInt("verifiedBy");

			fieldName = "tagname";
			UserTag t = new UserTag();
			t.setUsertagName(obj.getString("tagname"));
			this.id.tag = t;

		} catch (JSONException e) {
			e.printStackTrace();
			return "Missing non-optional field in JSON Request " + fieldName + ".";
		}
		return null;
	}

	@Embeddable
	static class UidTid implements Serializable {

		@Column(name = "userid")
		private Integer uid;

		@ManyToOne(cascade = CascadeType.ALL)
		@JoinColumn(name = "tagid", referencedColumnName = "tid")
		private UserTag tag;

		public Integer getUid() {
			return uid;
		}

		public void setUid(Integer uidRequest) {
			this.uid = uidRequest;
		}


		public UserTag getTag() {
			return tag;
		}

		public void setTag(UserTag tag) {
			this.tag = tag;
		}
	}

	public int getTid(){
		return this.id.tag.getUserTid();
	}
}
