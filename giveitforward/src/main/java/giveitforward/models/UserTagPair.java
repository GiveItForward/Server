package giveitforward.models;

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

	public UidTid getId() {
		return id;
	}

	public void setId(Integer uid, UserTag tag) {
		id = new UidTid();
		id.setTag(tag);
		id.setUid(uid);
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
		}
		else {
			object.put("tagname", this.id.tag.getUsertagName());
		}

		if (this.verifiedBy == null) {
			object.put("verifiedBy", "");
		}
		else {
			object.put("verifiedBy", this.verifiedBy);
		}

		if (this.time == null) {
			object.put("timeLimit", "");
		}
		else {
			object.put("timeLimit", this.time);
		}

		return object;
	}

	public boolean populateFromJSON(JSONObject obj) {
		return false;
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
}
