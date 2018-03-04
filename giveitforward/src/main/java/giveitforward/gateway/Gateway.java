package giveitforward.gateway;

import giveitforward.managers.*;
import giveitforward.models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
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
		String err = "Unable to create user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();
		User userResult = manager.signupUser(newUser);


		if(userResult == null){
			//error
			return manageUserResponse(err, userResult);
		}
//		boolean confirmed = ManageEmail.sendConfirmEmail(userResult);
//		if (!confirmed){
			// //TODO: When we want to release this we will uncomment.
//			manager.deleteUser(userResult);
//			return GIFResponse.getFailueResponse("Failed to send confirmation email.");
//		}

		//Add tags to user
		for (Object obj : userJSON.getJSONArray("tags")) {
			UserTag tag = new UserTag();
			JSONObject ob = (JSONObject)obj;
			tag.setUsertagName(ob.getString("tagname"));
			UserTagPair newTag = new ManageUserTag().AddTagToUser(tag, userResult);
			if(newTag == null){
				//error
			}
			else{
				userResult.addTag(newTag);
			}
		}

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

	@PUT
	@Path("/users/update")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to update user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();

		//Add tags to user
		for (Object obj : userJSON.getJSONArray("tags")) {
			UserTag tag = new UserTag();
			JSONObject ob = (JSONObject) obj;
			tag.setUsertagName(ob.getString("tagname"));
			try {
				tag.setUserTid(ob.getInt("tid"));
			}
			catch(JSONException e){
				tag = new ManageUserTag().getTagByTagname(tag.getUsertagName());
			}
			int uid = newUser.getUid();
			int tid = tag.getUserTid();
			UserTagPair newTag = new ManageUserTag().getUserTagPair(uid, tid);
			if (newTag == null) {
				//error
				//create a new tag.
				newTag = new UserTagPair(newUser.getUid(), tag);
			}
			newUser.addTag(newTag);
		}

		User userResult = manager.updateUser(newUser);

		if(userResult == null){
			//error
			return manageUserResponse(err, userResult);
		}

		return manageUserResponse(err, userResult);
	}

	// TODO - this is to deactivate a users account (put an inactive date and remove them from system visibility)
	@DELETE
	@Path("/users/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to delete user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();
		User currentUser = manager.getUserfromUID(newUser.getUid());
		currentUser.setInactivedate(new Timestamp(System.currentTimeMillis()));

		User userResult = manager.updateUser(currentUser);

		if(userResult == null){
			//error
			return manageUserResponse(err, userResult);
		}

		return manageUserResponse(err, userResult);
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

	/**
	 * Get users private information, aka ignore information that we don't want other users to see.
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/users/byuid/private")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrivateUserByUid(@Context HttpHeaders headers) {

		Integer uid = new Integer(headers.getRequestHeader("uid").get(0));
		String err = "unable to get user with uid " + uid;

		ManageUser manager = new ManageUser();
		User user = manager.getUserfromUID(uid);

		if (user != null) {

			JSONObject privateUser = user.asRequestJSON();

			ManageRequest reqManager = new ManageRequest();
			int numOfDonations = reqManager.getCountDonationsByUID(user.getUid());
			int numOfFulfilledRequests = reqManager.getCountRequestsByUID(user.getUid());

			privateUser.put("donateCount", numOfDonations);
			privateUser.put("receiveCount", numOfFulfilledRequests);

			return GIFResponse.getSuccessObjectResponse(privateUser.toString());
		}
		else {
			return GIFResponse.getFailueResponse(err);
		}
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

	// This is not the right thing to be doing... What if the user makes the payment but doesn't return to the site?
	// PayPal has to have something for this. There must be a way for us to tell paypal we want to be notified when a user
	// cancels or completes a payment.
	@GET
	@Path("/users/gethash")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersHash(@Context HttpHeaders headers) {
		String uid = headers.getRequestHeader("uid").get(0);

		ManageUser manager = new ManageUser();
		User user = manager.getUserfromUID(Integer.parseInt(uid));
		EmailCode ec = ManageEmail.getHash(user, 'D');

		String err = "unable to get hash.";

		if(ec == null)
		{
			return GIFResponse.getFailueResponse(err);
		}
		return GIFResponse.getSuccessObjectResponse(ec.asJSON().toString());
	}

	@GET
	@Path("/users/verifyhash")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyUsersHash(@Context HttpHeaders headers) {
		String hash = headers.getRequestHeader("hash").get(0);

		ManageUser manager = new ManageUser();
		EmailCode ec = ManageEmail.confirmHash(hash);

		String err = "unable to confirm hash.";

		if(ec == null)
		{
			return GIFResponse.getFailueResponse(err);
		}
		User user = manager.getUserfromUID(ec.getUid());

		return manageUserResponse(err, user);
	}

	@GET
	@Path("/users/deletehash")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsersHash(@Context HttpHeaders headers) {
		String hash = headers.getRequestHeader("hash").get(0);

		ManageUser manager = new ManageUser();
		EmailCode ec = ManageEmail.confirmHash(hash);

		String err = "unable to delete hash.";

		if(ec == null)
		{
			return GIFResponse.getFailueResponse(err);
		}

		boolean status = ManageEmail.deleteHash(ec);

		if(status == false)
		{
			return GIFResponse.getFailueResponse(err);
		}

		User user = manager.getUserfromUID(ec.getUid());
		return manageUserResponse(err, user);
	}

	@PUT
	@Path("/users/promote/org")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response promoteUserOrg(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to promote user to an organization user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();
		Boolean status = manager.promoteUserOrg(newUser);

		if(status == false){
			//error
			return manageUserResponse(err, newUser);
		}

		addTagsToUser(userJSON, newUser);

		return manageUserResponse(err, newUser);
	}

	@PUT
	@Path("/users/promote/admin")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response promoteUserAdmin(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to promote user to an organization user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();

		boolean status = manager.promoteUserAdmin(newUser);

		if(status == false){
			//error
			return manageUserResponse(err, newUser);
		}
		newUser.setAdmin(true);
		addTagsToUser(userJSON, newUser);

		return manageUserResponse(err, newUser);
	}

	@PUT
	@Path("/users/demote/org")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response demoteUserOrg(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to promote user to an organization user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();

		boolean status = manager.demoteUserOrg(newUser);

		if(status == false){
			//error
			return manageUserResponse(err, newUser);
		}

		addTagsToUser(userJSON, newUser);

		return manageUserResponse(err, newUser);
	}

	@PUT
	@Path("/users/demote/admin")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response demoteUserAdmin(String userJSon)//(@Context HttpHeaders headers)
	{
		String err = "Unable to promote user to an organization user.";

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();

		boolean status = manager.demoteUserAdmin(newUser);

		if(status == false){
			//error
			return manageUserResponse(err, newUser);
		}
		newUser.setAdmin(false);
		addTagsToUser(userJSON, newUser);

		return manageUserResponse(err, newUser);
	}

	@PUT
	@Path("/users/verifytag")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyUserTag(@Context HttpHeaders headers)
	{
		Integer uid;
		Integer oid;
		Integer tid;
		try {
			uid = new Integer(headers.getRequestHeader("uid").get(0));
			oid = new Integer(headers.getRequestHeader("oid").get(0));
			tid = new Integer(headers.getRequestHeader("tid").get(0));
		}
		catch (Exception e){
			return manageErrorResponse("Invalid/missing headers. Expecting uid, oid, tid.");
		}

		String err = "Unable to verify users tag.";

		User newUser = new ManageUser().verifyTag(uid, oid, tid);

		return manageUserResponse(err, newUser);
	}


	/********************************* ORG PATHS *******************************************/
	/**
	 * Returns a list of all approved organizations.
	 */
	@GET
	@Path("/organizations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganizations() {
		ManageOrganization manager = new ManageOrganization();
		List<Organization> orgs = manager.getAllOrgs();

		String err = "unable to fetch orgs";

		return manageCollectionResponse(err, orgs);
	}

	/**
	 * Returns a list of all organizations pending approval.
	 */
	@GET
	@Path("/organizations/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPendingOrganizations() {
		ManageOrganization manager = new ManageOrganization();
		List<Organization> orgs = manager.getAllPendingOrgs();

		String err = "unable to fetch pending orgs";

		return manageCollectionResponse(err, orgs);
	}

	/**
	 * Creates and adds a new org to the DB
	 */
	@POST
	@Path("/organizations/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newOrganization(String orgJson, @Context HttpHeaders headers) {
		String err = "Unable to create organization.";

		Integer uid = new Integer(headers.getRequestHeader("uid").get(0));

		Organization newOrg = new Organization();
		JSONObject orgJSON = new JSONObject(orgJson);
		String errorResult = newOrg.populateFromJSON(orgJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageOrganization manager = new ManageOrganization();
		Organization orgResult = manager.createOrganization(newOrg, uid);

		return manageObjectResponse(err, orgResult);
	}

	/**
	 *	Updates an existing org in the DB
	 */
	@PUT
	@Path("/organizations/update")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganization(String orgJson)
	{
		String err = "Unable to update organization.";

		Organization newOrg = new Organization();
		JSONObject orgJSON = new JSONObject(orgJson);
		String errorResult = newOrg.populateFromJSON(orgJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageOrganization manager = new ManageOrganization();

		Organization orgResult = manager.updateOrganization(newOrg);

		return manageObjectResponse(err, orgResult);
	}

	/**
	 * Sets the inactive date of an existing org to remove visibility
	 */
	@DELETE
	@Path("/organizations/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(String orgJson)
	{
		String err = "Unable to delete org.";

		Organization newOrg = new Organization();
		JSONObject orgJSON = new JSONObject(orgJson);
		String errorResult = newOrg.populateFromJSON(orgJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageOrganization manager = new ManageOrganization();
		Organization orgResult = manager.deleteOrganization(newOrg);

		return manageObjectResponse(err, orgResult);
	}

    /**
     * Approve an organization
     */
	@PUT
    @Path("organizations/approve")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveOrganization(String orgJson) {
	    String err = "Unable to approve org.";

	    Organization newOrg = new Organization();
	    JSONObject orgJSON = new JSONObject(orgJson);
		String errorResult = newOrg.populateFromJSON(orgJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

	    ManageOrganization manager = new ManageOrganization();
	    Organization orgResult = manager.approveOrganization(newOrg);

	    return manageObjectResponse(err, orgResult);
    }

    /**
     * Get a singular org by oid
     */
    @GET
    @Path("/organizations/byoid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrgbyOID(@Context HttpHeaders headers) {
        Integer oid = new Integer(headers.getRequestHeader("oid").get(0));
        ManageOrganization manager = new ManageOrganization();
        Organization org = manager.getOrgByOrgId(oid);

        String err = "unable to get user with uid " + oid;

        return manageObjectResponse(err, org);
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
		String errorResult = newRequest.populateFromJSON(new JSONObject(requestJSON));

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageRequest manager = new ManageRequest();
		Request requestResult = manager.createRequest(newRequest);

		String err = "Unable to create request";

		return manageObjectResponse(err, requestResult);
	}

	@PUT
	@Path("/requests/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRequest(String requestJSON) {
		Request updateRequest = new Request();
		String errorResult = updateRequest.populateFromJSON(new JSONObject(requestJSON));

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageRequest manager = new ManageRequest();
		Request requestResult = manager.updateRequest(updateRequest);

		String err = "Unable to update request";

		return manageObjectResponse(err, requestResult);
	}

	@DELETE
	@Path("/requests/delete")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRequest(@Context HttpHeaders headers) {

		String rid = headers.getRequestHeader("rid").get(0);

		ManageRequest manager = new ManageRequest();
		Request deleteRequest = manager.getRequestByRid(rid).get(0);
		Request requestResult = manager.deleteRequest(deleteRequest);

		String err = "Unable to delete request";

		return manageObjectResponse(err, requestResult);
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

	@GET
	@Path("/requests/rid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestByRid(@Context HttpHeaders headers) {
        String rid = headers.getRequestHeader("rid").get(0);

        ManageRequest manager = new ManageRequest();
        List<Request> requests = manager.getRequestByRid(rid);

        String err = "unable to fetch request with the rid " + rid;

        return manageCollectionResponse(err, requests);
	}

    @GET
    @Path("/requests/fulfill")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fullfillRequest(@Context HttpHeaders headers) {
        String rid = headers.getRequestHeader("rid").get(0);
        String duid = headers.getRequestHeader("duid").get(0);

        ManageRequest manager = new ManageRequest();
        boolean fulfilledRequest = manager.fulfillRequest(Integer.parseInt(rid), Integer.parseInt(duid));

        if (fulfilledRequest){
            System.out.println("Request has been fulfilled *****************************" +
                    "***************************************************************************************" +
                    "***************************************************************************************" +
                    "***************************************************************************************");
        }

        String err = "unable to fulfill request with the rid " + rid + "and duid + " + duid;

        List<Request> fulfilledRequestModel = manager.getRequestByRid(rid);

        return manageCollectionResponse(err, fulfilledRequestModel);
    }

    @GET
	@Path("/requests/filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterRequest(String reqJson) {
		String err = "unable to fetch filtered requests";

//		JSONObject reqJSON = new JSONObject(headers.getRequestHeader("rtags"));
		JSONObject reqJSON = new JSONObject(reqJson);

		ManageRequest manager = new ManageRequest();
		List<RequestTag> reqTags = new ArrayList<RequestTag>();
		List<UserTag> userTags = new ArrayList<UserTag>();

//		String age = headers.getRequestHeader("age").get(0);
//		String price = headers.getRequestHeader("price").get(0);

		String age = reqJSON.getString("age");
		String price = reqJSON.getString("price");

		for (Object obj : reqJSON.getJSONArray("rtags")) {
			RequestTag tag = new RequestTag();
			JSONObject ob = (JSONObject)obj;
			tag.setRequestTagname(ob.getString("tagname"));
			tag.setRequestTid(ob.getInt("tid"));
			reqTags.add(tag);
		}

		for (Object obj : reqJSON.getJSONArray("utags")) {
			UserTag tag = new UserTag();
			JSONObject ob = (JSONObject) obj;
			tag.setUsertagName(ob.getString("tagname"));
			tag.setUserTid(ob.getInt("tid"));
			userTags.add(tag);
		}
		List<Request> filterRequestModel = manager.getRequestsFilterByTags(reqTags, userTags, age, price);

		return manageCollectionResponse(err,filterRequestModel);
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

	/*********************************** ThankYou PATHS *****************************************/

	@GET
	@Path("/thankyou")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllThankYous() {
		ManageThankYou manager = new ManageThankYou();
		List<ThankYou> thankYous = manager.getAllThankYous();

		String err = "unable to fetch ThankYous";

		return manageCollectionResponse(err, thankYous);
	}

	@POST
	@Path("/thankyou/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createThankYou(String thankYouJSON) {
		ManageThankYou manager = new ManageThankYou();
		ThankYou thankYou = new ThankYou();
		String errorResult = thankYou.populateFromJSON(new JSONObject(thankYouJSON));

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		thankYou = manager.createThankYou(thankYou);

		String err = "unable to create ThankYous";

		return manageObjectResponse(err, thankYou);
	}

	@PUT
	@Path("/thankyou/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createThankYou2(String thankYouJSON) {
		return createThankYou(thankYouJSON);
	}

	@PUT
	@Path("/thankyou/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateThankYou(String thankYouJSON) {
		ManageThankYou manager = new ManageThankYou();
		ThankYou thankYou = new ThankYou();
		String errorResult = thankYou.populateFromJSON(new JSONObject(thankYouJSON));

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		thankYou = manager.updateThankYou(thankYou);

		String err = "unable to update ThankYous";

		return manageObjectResponse(err, thankYou);
	}

	@DELETE
	@Path("/thankyou/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteThankYou(String thankYouJSON) {
		ManageThankYou manager = new ManageThankYou();
		ThankYou thankYou = new ThankYou();
		String errorResult = thankYou.populateFromJSON(new JSONObject(thankYouJSON));

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		thankYou = manager.deleteThankYou(thankYou);

		String err = "unable to delete ThankYous";

		return manageObjectResponse(err, thankYou);
	}

	/*********************************** Notification PATHS **************************************/
	@GET
	@Path("/notifications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(@Context HttpHeaders headers) {
		String uid = headers.getRequestHeader("uid").get(0);

		ManageNotification manager = new ManageNotification();
		List<Notification> notifications = manager.getAllUnreadNotifications(Integer.parseInt(uid));

		String err = "unable to fetch notifications for the user: " + uid;

		return manageCollectionResponse(err, notifications);
	}

	@PUT
	@Path("/notifications/seen")
	@Produces(MediaType.APPLICATION_JSON)
	public Response seenOneNotification(@Context HttpHeaders headers) {
        String nid = headers.getRequestHeader("nid").get(0);
		ManageNotification manager = new ManageNotification();
		Notification note = manager.seenNotification(Integer.parseInt(nid));

		String err = "could not set nid: " + nid + " to seen.";

		return manageObjectResponse(err, note);
	}

	@PUT
	@Path("/notifications/seenall")
	@Produces(MediaType.APPLICATION_JSON)
	public Response seenAllNotifications(@Context HttpHeaders headers) {
		String uid = headers.getRequestHeader("uid").get(0);

		ManageNotification manager = new ManageNotification();
		List<Notification> notes = manager.seenAllNotifications(Integer.parseInt(uid));

		String err = "could not set 1 or more notifications for: " + uid + " to seen.";

		return manageCollectionResponse(err, notes);
	}

	/*********************************************** Helpers *************************************/
	private int getYear(Timestamp t) {//your object here.
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(t.getTime()));
		return cal.get(Calendar.YEAR);
	}

	private Response manageErrorResponse(String err){
		return GIFResponse.getFailueResponse(err);
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

	private Response manageObjectResponse(String err, Model object) {
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

	private void addTagsToUser(JSONObject userJSON, User newUser){
		//Add tags to user
		for (Object obj : userJSON.getJSONArray("tags")) {
			UserTag tag = new UserTag();
			JSONObject ob = (JSONObject) obj;
			tag.setUsertagName(ob.getString("tagname"));
			try {
				tag.setUserTid(ob.getInt("tid"));
			}
			catch(JSONException e){
				tag = new ManageUserTag().getTagByTagname(tag.getUsertagName());
			}
			UserTagPair newTag = new ManageUserTag().getUserTagPair(newUser.getUid(), tag.getUserTid() );
			if (newTag == null) {
				//error
			}
			else {
				newUser.addTag(newTag);
			}
		}
	}
}
