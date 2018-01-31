package giveitforward.gateway;

import giveitforward.managers.*;
import giveitforward.models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Path("/")
public class Gateway {
	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	public Response test() {
		return Response.status(200).entity("Hi! Welcome to Give It Forward.").build();
	}


	/********************************* User PATHS *******************************************/
	@GET
	@Path("/users/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userLogin(@Context HttpHeaders headers) {

		String username = headers.getRequestHeader("email").get(0);
		String password = headers.getRequestHeader("password").get(0);

		ManageUser manager = new ManageUser();
		User userResult = manager.loginUser(username, password);

		String err = "Unable to log in user.";

		return manageUserResponse(err, userResult);
	}


	// this method is for /signup a new user
	@POST
	@Path("/users/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newUser(String userJSon)//(String userJson)
	{
		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		newUser.populateSignupUserFromJSON(userJSON);

		ManageUser manager = new ManageUser();
		User userResult = manager.signupUser(newUser);



//		boolean confirmed = ManageEmail.sendConfirmEmail(userResult);
//		if (!confirmed){
//			TODO: When we want to release this we will uncomment.
			//manager.deleteUser(userResult);
			//return GIFResponse.getFailueResponse("Failed to send confirmation email.");
		//}

		//Add tags to user
		for (Object obj : userJSON.getJSONArray("tags")) {
			UserTag tag = new UserTag();
			JSONObject ob = (JSONObject)obj;
			tag.setUsertagName(ob.getString("tagname"));
			new ManageUserTag().AddTagToUser(tag, userResult);
		}

		String err = "Unable to create user.";

		return manageUserResponse(err, userResult);
	}

	//response is to login the user?
	@GET
	@Path("/confirm/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmEmail(@Context HttpHeaders headers, @PathParam("id") String emailHash) {
		User userResult = ManageEmail.confirmEmail(emailHash);

		String err = "Confirmation failed";

		return manageUserResponse(err, userResult);
	}

	// TODO - this is to update a user
	@PUT
	@Path("/users/update")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response putNewUser()//(@Context HttpHeaders headers)
	{
		return Response.ok()
				.entity("HI".toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.build();
	}

	// TODO - this is to deactivate a users account (put an inactive date and remove them from system visibility)
	@DELETE
	@Path("/users/delete")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser()//(@Context HttpHeaders headers)
	{
		return Response.ok()
				.entity("HI".toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.build();
	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(@Context HttpHeaders headers) {
		ManageUser manager = new ManageUser();
		List<User> users = manager.getAllUsers();

		String err = "Unable to get all users";

		return manageCollectionResponse(err, users);
	}

	@GET
	@Path("/users/byuid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUid(@Context HttpHeaders headers) {
		Integer uid = new Integer(headers.getRequestHeader("uid").get(0));
		ManageUser manager = new ManageUser();
		User user = manager.getUserfromUID(uid);

		String err = "unable to get user with uid " + uid;

		return manageUserResponse(err, user);
	}


	@GET
	@Path("/users/getdonateamount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDonateAmount(@Context HttpHeaders headers) {
		String uid = headers.getRequestHeader("uid").get(0);

		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getRequestsFilterByDonateUid(uid);
		String err = "unable to get donations";
		if(requests == null)
		{
			return GIFResponse.getFailueResponse(err);
		}

		Double donations = getDonateAmmount(requests);
		JSONObject jsonOb = new JSONObject();
		jsonOb.put("donateAmount", donations);

		return GIFResponse.getSuccessObjectResponse(jsonOb.toString());
	}


	/********************************* ORG PATHS *******************************************/
	/**
	 * Returns a list of all approved organizations.
	 */
	@GET
	@Path("/organizations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganizations(@Context HttpHeaders headers) {
		ManageOrganization manager = new ManageOrganization();
		List<Organization> orgs = manager.getAllOrgs();

		String err = "unable to fetch orgs";

		return manageCollectionResponse(err, orgs);
	}


	/******************************* REQUEST PATHS *****************************************/
	@GET
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestFeed(@Context HttpHeaders headers) {
		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getOpenRequests();

		String err = "unable to fetch requests";

		return manageCollectionResponse(err, requests);
	}

	@GET
	@Path("/requests/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestFeedAll(@Context HttpHeaders headers) {
		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getAllRequests();

		String err = "unable to fetch requests";

		return manageCollectionResponse(err, requests);
	}

	//MyDonations
	@GET
	@Path("/requests/donateuid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestFeedFilterByDonateUid(@Context HttpHeaders headers) {
		String dUid = headers.getRequestHeader("uid").get(0);

		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getRequestsFilterByDonateUid(dUid);

		String err = "unable to fetch requests with the duid " + dUid;

		return manageCollectionResponse(err, requests);
	}


	//MyRequests
	@GET
	@Path("/requests/requestuid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestFeedFilterByRequestUid(@Context HttpHeaders headers) {
		String rUid = headers.getRequestHeader("uid").get(0);

		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getRequestsFilterByRequestUid(rUid);

		String err = "unable to fetch requests with the ruid " + rUid;

		return manageCollectionResponse(err, requests);
	}


	@GET
	@Path("/requests/requestuid/open")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestFeedFilterByRequestUidOpen(@Context HttpHeaders headers) {
		String rUid = headers.getRequestHeader("uid").get(0);

		ManageRequest manager = new ManageRequest();
		List<Request> requests = manager.getRequestsFilterByRequestUidOpen(rUid);

		String err = "unable to fetch open requests with the ruid " + rUid;

		return manageCollectionResponse(err, requests);
	}

	@POST
	@Path("/requests/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newRequest(String requestJSON) {
		Request newRequest = new Request();
		newRequest.populateFromJSON(new JSONObject(requestJSON));

		ManageRequest manager = new ManageRequest();
		Request requestResult = manager.createRequest(newRequest);

		String err = "Unable to create request";

		return manageObjectResonse(err, requestResult);
	}

	@GET
	@Path("/requests/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestTags(@Context HttpHeaders headers) {
		ManageRequestTag manager = new ManageRequestTag();
		List<RequestTag> tags = manager.getAllRequestTags();

		String err = "unable to fetch tags";

		return manageCollectionResponse(err, tags);
	}


	/*********************************** Tag PATHS *****************************************/

	@GET
	@Path("/tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTags() {
		ManageUserTag manager = new ManageUserTag();
		List<UserTag> tags = manager.getAllTags();

		String err = "unable to fetch tags";

		return manageCollectionResponse(err, tags);
	}


	/*********************************************** Helpers *************************************/

	private int getYear(Timestamp t) {//your object here.
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(t.getTime()));
		return cal.get(Calendar.YEAR);
	}

	private Response manageCollectionResponse(String err, List objs) {
		if (objs != null) {
			String objJSON = Model.asJSONCollection(objs).toString();
			return GIFResponse.getSuccessObjectResponse(objJSON);
		}
		else {
			return GIFResponse.getFailueResponse(err);
		}
	}

	private Response manageUserResponse(String err, User user) {
		if (user != null) {

			ManageRequest reqManager = new ManageRequest();
			int numOfDonations = reqManager.getCountDonationsByUID(user.getUid());
			int numOfFulfilledRequests = reqManager.getCountRequestsByUID(user.getUid());

			JSONObject jsonUser = user.asJSON();

			jsonUser.put("donateCount", numOfDonations);
			jsonUser.put("receiveCount", numOfFulfilledRequests);

			return GIFResponse.getSuccessObjectResponse(jsonUser.toString());
		}
		else {
			return GIFResponse.getFailueResponse(err);
		}
	}

	private Response manageObjectResonse(String err, Model object) {
		if (object != null) {
			String objJSON = object.asJSON().toString();
			return GIFResponse.getSuccessObjectResponse(objJSON);
		}
		else {
			return GIFResponse.getFailueResponse(err);
		}
	}

	private Double getDonateAmmount(List<Request> requests) {
		double donate_amount = 0.0;

		//current year
		int current_year = Calendar.getInstance().get(Calendar.YEAR);

		for (Request r : requests) {
			if (getYear(r.getDonateTime()) == current_year) {
				donate_amount += r.getAmount();
			}
		}
		return donate_amount;
	}
}
