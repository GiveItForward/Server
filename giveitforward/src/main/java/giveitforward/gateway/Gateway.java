package giveitforward.gateway;

import giveitforward.managers.*;
import giveitforward.models.*;
import giveitforward.models.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Timestamp;
import java.util.*;


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
		String err = "Unable to log in user.";

		String email = headers.getRequestHeader("email").get(0);


		boolean google;
		try {
			google = Boolean.parseBoolean(headers.getRequestHeader("google").get(0));
		} catch (Exception e) {
			google = false;
		}

		ManageUser manager = new ManageUser();

		User userResult;
		GIFOptional result;
		if (google) {
			String password = headers.getRequestHeader("token").get(0);
			password = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password + "supercalifragilisticexpialidocious");
			result = manager.loginGoogleUser(password);
		} else {
			String password = headers.getRequestHeader("password").get(0);
			password = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password + "supercalifragilisticexpialidocious");
			result = manager.loginUser(email, password);
		}

		if(result.getObject() != null){
			userResult = (User)result.getObject();
			return manageUserResponse(err, userResult);
		}
		else {
			err = result.getErrorMessage();
			return manageErrorResponse(err);
		}
	}


	// this method is for /signup a new user
	@POST
	@Path("/users/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newUser(@Context HttpHeaders headers, String userJSon)//(String userJson)
	{
		String err = "Unable to create user.";

        boolean google;
        try {
			google = Boolean.parseBoolean(headers.getRequestHeader("google").get(0));
		} catch (Exception e) {
        	google = false;
		}

		User newUser = new User();
		JSONObject userJSON = new JSONObject(userJSon);
		System.out.println();

        // todo - this is wrong. We need to generate a new salt for every user and put it in the database alongside the hash
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(userJSON.get("password") + "supercalifragilisticexpialidocious");
        userJSON.put("password", sha256hex);

		String errorResult = newUser.populateFromJSON(userJSON);

		//Trouble creating from JSON
		if(errorResult != null) {
			return manageErrorResponse(errorResult);
		}

		ManageUser manager = new ManageUser();

		if(manager.getUserFromEmail(newUser.getEmail()) != null){
			return manageErrorResponse("An account with this email already exists.");
		}
		else if(manager.getUserFromUsername(newUser.getUsername()) != null){
			return manageErrorResponse("This username has been taken.");
		}

		User userResult = manager.signupUser(newUser);

		if(userResult == null){
			//error
			return manageUserResponse(err, userResult);
		}

		if (!google) {
            boolean confirmed = ManageEmail.sendConfirmEmail(userResult);
            if (!confirmed) {
                manager.deleteUser(userResult);
                return GIFResponse.getFailueResponse("Failed to send confirmation email.");
            }
        } else {
			manager.confirmEmail(userResult.getUid());
		}

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


	@GET
	@Path("/confirm/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmEmail(@Context HttpHeaders headers, @PathParam("id") String emailHash) {
		User userResult = ManageEmail.confirmEmail(emailHash);

		String err = "Confirmation failed";

		return manageUserResponse(err, userResult);
	}


	@GET
	@Path("/forgotpassword")
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgotPassword(@Context HttpHeaders headers) {
		String email = headers.getRequestHeader("email").get(0);
		boolean result = ManageEmail.forgotPassword(email);

		if (result == false){
			return manageErrorResponse("Password reset, unable to send.");
		}
		else {
			return GIFResponse.getSuccessObjectResponse("Passoword reset sent!");
		}
	}

	@GET
	@Path("/resetpassword")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(@Context HttpHeaders headers){
		String hash = headers.getRequestHeader("hash").get(0);
		String newPassword = headers.getRequestHeader("password").get(0);
		String password = org.apache.commons.codec.digest.DigestUtils.sha256Hex(newPassword + "supercalifragilisticexpialidocious");
		User userResult = ManageEmail.resetPassword(hash, password);

		String err = "Unable to reset password. Please contact an admin for assistance.";

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
		try{
			userJSON.getJSONArray("tags");
		}
		catch(JSONException e){

			userJSON.put("tags", new JSONArray());
		}

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
			if(tag == null){
				continue;
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
    @Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@Context HttpHeaders headers)//(@Context HttpHeaders headers)
	{

        String err = "Unable to delete user.";

        String uid = headers.getRequestHeader("uid").get(0);

        ManageUser manager = new ManageUser();
        User user = manager.getUserfromUID(Integer.parseInt(uid));

        user.setInactivedate(new Timestamp(System.currentTimeMillis()));

		User userResult = manager.updateUser(user);

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
    @Path("/users/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActiveUsers(@Context HttpHeaders headers) {
        ManageUser manager = new ManageUser();
        List<User> users = manager.getAllActiveUsers();

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
		newUser = manager.getUserfromUID(newUser.getUid());
//		addTagsToUser(userJSON, newUser);

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

	@GET
	@Path("/users/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsers(@Context HttpHeaders headers)
	{
		String match = headers.getRequestHeader("search").get(0);

		String err = "Unable to search for users.";
		ManageUser manager = new ManageUser();
		List<User> users = manager.searchForUser(match);

		return manageCollectionResponse(err, users);
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(@Context HttpHeaders headers)
	{
		Integer oid = Integer.parseInt(headers.getRequestHeader("oid").get(0));

		String err = "Unable to delete org.";

		ManageOrganization manager = new ManageOrganization();
		Organization orgResult = manager.deleteOrganization(oid);

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

        String err = "unable to get org with oid " + oid;

        return manageObjectResponse(err, org);
    }

	@GET
	@Path("/organizations/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchOrgs(@Context HttpHeaders headers)
	{
		String match = headers.getRequestHeader("search").get(0);

		String err = "Unable to search for orgs.";
		ManageOrganization manager = new ManageOrganization();
		List<Organization> orgs = manager.searchForOrg(match);

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
	public Response filterRequest(@Context HttpHeaders headers) {

		String err = "unable to fetch filtered requests";

		ManageRequest manager = new ManageRequest();
		List<RequestTag> reqTags = new ArrayList<RequestTag>();
		List<UserTag> userTags = new ArrayList<UserTag>();

        String age = "", price = "";
        try {
        	age = headers.getRequestHeader("age").get(0);
        } catch(Exception e) {}
        try {
        	price = headers.getRequestHeader("price").get(0);
        } catch (Exception e) {}


        String rtagString = "", utagString = "";
        try {
            rtagString = headers.getRequestHeader("rtags").get(0);
            utagString = headers.getRequestHeader("utags").get(0);
        } catch (Exception e) {}

        if (!rtagString.isEmpty()) {
            String[] rtags = rtagString.split(",");
            for (int i = 0; i < rtags.length; i++) {
                RequestTag tag = new RequestTag();
                tag.setRequestTid(Integer.parseInt(rtags[i].trim()));
                reqTags.add(tag);
            }
        }
        if (!utagString.isEmpty()) {
            String[] utags = utagString.split(",");
            for (int i = 0; i < utags.length; i++) {
                UserTag tag = new UserTag();
                tag.setUserTid(Integer.parseInt(utags[i].trim()));
                userTags.add(tag);
            }
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

	@POST
	@Path("/svgavatars")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response avatarSave(@Context HttpHeaders headers, String data) {
		String uid = headers.getRequestHeader("uid").get(0);

		ManageUser mu = new ManageUser();
		User u = mu.updatePic(uid, data);

		String err = "could not update user profile pic";

		return manageObjectResponse(err, u);
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
