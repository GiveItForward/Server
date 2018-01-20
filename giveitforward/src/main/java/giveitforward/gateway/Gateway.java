package giveitforward.gateway;

import giveitforward.managers.ManageOrganization;
import giveitforward.managers.ManageRequest;
import giveitforward.managers.ManageUser;
import giveitforward.managers.ManageUserTag;
import giveitforward.models.Organization;
import giveitforward.models.Request;
import giveitforward.models.User;
import giveitforward.models.UserTag;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

@Path("/")
public class Gateway
{
    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response test()
    {
        return Response.status(200).entity("Hi! Welcome to Give It Forward.").build();
    }


    /********************************* User PATHS *******************************************/
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@Context HttpHeaders headers)
    {

        String username = headers.getRequestHeader("email").get(0);
        String password = headers.getRequestHeader("password").get(0);

        ManageUser manager = new ManageUser();
        User userResult = manager.loginUser(username, password);

        ManageUserTag tagManager = new ManageUserTag();
        List<String> tags = tagManager.getAllTagsByUID(userResult.getUid());

        ManageRequest reqManager = new ManageRequest();
        int numOfDonations = reqManager.getCountDonationsByUID(userResult.getUid());
        int numOfFulfilledRequests = reqManager.getCountRequestsByUID(userResult.getUid());


        if (userResult != null)
        {
            return getUserObjectResponse(userResult, tags, numOfDonations, numOfFulfilledRequests);

        } else
        {
            return Response.serverError()
                    .entity("Logged in user : false")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        }
    }


    /**
     * This method is needed because the browser sends /signup first an OPTION method request in
     * order to figure out what kind of methods the server allows.
     * @return An ok response with
     */
    @OPTIONS
    @Path("/login")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginOptions()//(@Context HttpHeaders headers)
    {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, email, password, uid")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Allow", "GET, POST, DELETE, PUT")
                .build();
    }


    // this method is for /signup a new user
    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(String userJSon)//(String userJson)
    {
        User newUser = GiveItForwardJSON.getUserFromJSON(new JSONObject(userJSon));

        ManageUser manager = new ManageUser();
        User userResult = manager.signupUser(newUser);

        ManageUserTag tagManager = new ManageUserTag();
        List<String> tags = tagManager.getAllTagsByUID(userResult.getUid());

        ManageRequest reqManager = new ManageRequest();
        int numOfDonations = reqManager.getCountDonationsByUID(userResult.getUid());
        int numOfFulfilledRequests = reqManager.getCountRequestsByUID(userResult.getUid());

        if(userResult == null){
            return Response.serverError()
                    .entity("Created user : false")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        } else {
            return getUserObjectResponse(userResult, tags, numOfDonations, numOfFulfilledRequests);
        }

    }

    private Response getUserObjectResponse(User userResult, List<String> tags, int numOfDonations, int numOfFulfilledRequests)
    {
        JSONObject jsonUser = GiveItForwardJSON.writeUserToJSON(userResult);

        if(tags != null && !tags.isEmpty()){
            jsonUser.put("tags", new JSONArray(tags));
        }

        jsonUser.put("donateCount", numOfDonations);
        jsonUser.put("receiveCount", numOfFulfilledRequests);

        return Response.ok()
                .entity(jsonUser.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, email, username, password, uid")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Allow", "GET, POST, DELETE, PUT")
                .build();
    }

    // TODO - this is to update a user
    @PUT
    @Path("/signup")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response putNewUser()//(@Context HttpHeaders headers)
    {
        return Response.ok()
                .entity("HI".toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();

    }

    // TODO - this is to deactiate a users account (put an inactive date and remove them from system visibility)
    @DELETE
    @Path("/signup")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser()//(@Context HttpHeaders headers)
    {
        return Response.ok()
                .entity("HI".toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();

    }

    /**
     * This method is needed because the browser sends /signup first an OPTION method request in
     * order to figure out what kind of methods the server allows.
     * @return An ok response with
     */
    @OPTIONS
    @Path("/signup")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response optionsNewUser()//(@Context HttpHeaders headers)
    {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Allow", "GET, POST, DELETE, PUT")
                .build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@Context HttpHeaders headers)
    {

        ManageUser manager = new ManageUser();
        List<User> userResult = manager.getAllUsers();

        if(userResult == null){
            return Response.ok()
                    .entity("Unable to get all users")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        } else {
            JSONArray userJson = GiveItForwardJSON.writeAllUsersToJSon(userResult);
            return Response.status(401)
                    .entity("Result of creating user true\n\n: " + userJson)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        }

    }


    /********************************* ORG PATHS *******************************************/
    /**
     * Returns a list of all approved organizations.
     */
    @GET
    @Path("/organizations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrganizations(@Context HttpHeaders headers)
    {

        ManageOrganization manager = new ManageOrganization();
        List<Organization> orgs = manager.getAllOrgs();

        JSONArray orgJSON = GiveItForwardJSON.getOrgJSONCollection(orgs);

        return Response.ok()
                .entity(orgJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }


    /******************************* REQUEST PATHS *****************************************/
    @GET
    @Path("/requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeed(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();
        List<Request> requests = manager.getAllRequests();

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @GET
    @Path("/requests/donateuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeedFilterByDonateUid(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();

        String dUid = headers.getRequestHeader("uid").get(0);
        List<Request> requests = manager.getRequestsFilterByDonateUid(dUid);

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }


    /**
     * This method is needed because the browser sends /signup first an OPTION method request in
     * order to figure out what kind of methods the server allows.
     * @return An ok response with
     */
    @OPTIONS
    @Path("/requests/donateuid")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response reqDonateOptions()//(@Context HttpHeaders headers)
    {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, email, password, uid")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .header("Allow", "GET, POST, DELETE, PUT")
                .build();
    }

    @GET
    @Path("/requests/requestuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeedFilterByRequestUid(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();
        String rUid = headers.getRequestHeader("uid").get(0);
        
        List<Request> requests = manager.getRequestsFilterByRequestUid(rUid);

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @GET
    @Path("/requests/requestuid/open")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeedFilterByRequestUidOpen(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();
        String rUid = headers.getRequestHeader("uid").get(0);
        List<Request> requests = manager.getRequestsFilterByRequestUidOpen(rUid);

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }


    /*********************************** Tag PATHS *****************************************/

    @GET
    @Path("/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTags()
    {

        ManageUserTag manager = new ManageUserTag();
        List<UserTag> tagResult = manager.getAllTags();

        if (tagResult != null)
        {
            JSONArray jsonTags = GiveItForwardJSON.writeTagsToJSon(tagResult);

            return Response.ok() //200
                    .entity(jsonTags.toString())
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();

        } else
        {
            return Response.status(401)
                    .entity("Call Failed")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        }
    }
}
