package giveitforward.models;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "request")
public class Request extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rid", unique = true, nullable = false)
	private int rid;

	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "image")
	private String image;

	@Column(name = "requesttime")
	private Timestamp requesttime;

	@Column(name = "donatetime")
	private Timestamp donateTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ruid")
	private User rUser;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tag1")
	private RequestTag tag1;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tag2")
	private RequestTag tag2;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rid")
	private ThankYou thankYou;

	@Column(name = "duid")
	private Integer duid;

	public Request(String description, Double amount, String image, User rUser, ThankYou thankYou, Integer duid) {
		this.description = description;
		this.amount = amount;
		this.image = image;
		this.rUser = rUser;
		//        this.thankYou = thankYou;
		this.duid = duid;
		this.requesttime = new Timestamp(System.currentTimeMillis());
	}

	public Request() {
	}


	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Timestamp getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Timestamp requesttime) {
		this.requesttime = requesttime;
	}

	public String asString() {
		String base = "rid: " + this.rid + ", amount: " + this.amount + ".";
		//        if (thankYou != null) {
		//            base += ", thankYou: true";
		//        } else {
		base += ", thankYou: false";
		//        }
		return base;
	}

	public JSONObject asJSON() {
		JSONObject object = new JSONObject();
		object.put("rid", this.rid);
		object.put("description", this.description);
		object.put("amount", this.amount);
		object.put("image", this.image);
		object.put("duid", this.duid);

		if (this.tag1 != null) {
			object.put("tag1", this.tag1.asJSON());
		}
		else {
			object.put("tag1", "");
		}

		if (this.tag2 != null) {
			object.put("tag2", this.tag2.asJSON());
		}
		else {
			object.put("tag2", "");
		}

		if (this.thankYou != null) {
			object.put("thankYou", this.thankYou.asJSON());
		}
		else {
			object.put("thankYou", "");
		}

		if (this.rUser == null) {
			object.put("rUser", new User().asJSON());
		}
		else {
			object.put("rUser", this.rUser.asRequestJSON());
		}

		object.put("requestTime", getDisplayDate(this.requesttime));
		object.put("donateTime", getDisplayDate(this.donateTime));
		return object;
	}

	public boolean populateFromJSON(JSONObject obj) {
		try {
			this.image = obj.getString("image");
			this.amount = obj.getDouble("amount");
			this.description = obj.getString("description");
			//            this.rUser = new User();
			//            JSONObject o = new JSONObject()
			//            this.rUser.populateFromJSON(obj.getJSONObject("rUser"));
			try {
				JSONObject tag1JSON = obj.getJSONObject("tag1");
				if (tag1JSON == null) {
					this.tag1 = null;
				}
				else {
					RequestTag tag1 = new RequestTag();
					tag1.populateFromJSON(tag1JSON);
					this.tag1 = tag1;
				}
			}
			catch (JSONException e) {
				this.tag1 = null;
			}

			try {
				JSONObject tag2JSON = obj.getJSONObject("tag2");
				if (tag2JSON == null) {
					this.tag2 = null;
				}
				else {
					RequestTag tag2 = new RequestTag();
					tag2.populateFromJSON(tag2JSON);
					this.tag2 = tag2;
				}
			}
			catch (JSONException e) {
				this.tag2 = null;
			}

			JSONObject userJSON = obj.getJSONObject("rUser");
			User user = new User();
			user.populateFromJSON(userJSON);
			this.rUser = user;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int getDuid() {
		return duid;
	}

	public void setDuid(int duid) {
		this.duid = duid;
	}

	public User getRequestor() {
		return rUser;
	}

	public void setRequestor(User requestor) {
		this.rUser = requestor;
	}

	public Timestamp getDonateTime() {
		return donateTime;
	}

	public void setDonateTime(Timestamp donateTime) {
		this.donateTime = donateTime;
	}

	public RequestTag getTag1() {
		return this.tag1;
	}

	public void setTag1(RequestTag tag1) {
		this.tag1 = tag1;
	}

	public RequestTag getTag2() {
		return this.tag2;
	}

	public void setTag2(RequestTag tag2) {
		this.tag2 = tag2;
	}

	public ThankYou getThankYou() {
		return this.thankYou;
	}

	public void setThankYou(ThankYou thankYou) {
		this.thankYou = thankYou;
	}
}
