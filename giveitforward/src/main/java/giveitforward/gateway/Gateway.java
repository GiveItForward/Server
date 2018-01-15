package giveitforward.gateway;

import giveitforward.managers.ManageOrganization;
import giveitforward.managers.ManageRequest;
import giveitforward.managers.ManageUser;
import giveitforward.managers.ManageUserTag;
import giveitforward.models.Organization;
import giveitforward.models.Request;
import giveitforward.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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


    @GET
    @Path("/login/{un}/{pw}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@PathParam("un") String username, @PathParam("pw") String password)
    {

//        String username = headers.getRequestHeader("username").get(0);
//        String password = headers.getRequestHeader("password").get(0);

        ManageUser manager = new ManageUser();
        User userResult = manager.loginUser(username, password);

        ManageUserTag tagManager = new ManageUserTag();
        List<String> tags = tagManager.getAllTagsByUID(userResult.getUid());

        int numOfDonations = manager.getNumberOfDontations(userResult.getUid());
        int numOfFulfilledRequests = manager.getNumberOfReceivedDonations(userResult.getUid());


        if (userResult != null)
        {
            JSONObject jsonUser = GiveItForwardJSON.writeUserToJSON(userResult);

            if(tags != null && !tags.isEmpty()){
                jsonUser.put("tags", new JSONArray(tags));
            }

            jsonUser.put("numDonations", numOfDonations);
            jsonUser.put("numFulfilledRequests", numOfFulfilledRequests);

            return Response.ok() //200
                    .entity(jsonUser.toString())
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();

        } else
        {
            return Response.status(401)
                    .entity("Logged in user : false")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        }
    }

    @GET
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(@Context HttpHeaders headers)
    {

        String username = headers.getRequestHeader("username").get(0);
        String password = headers.getRequestHeader("password").get(0);
        String email = headers.getRequestHeader("email").get(0);
//        String isAdminString = headers.getRequestHeader("isAdmin").get(0);
//        String oidString = headers.getRequestHeader("oid").get(0);
//        String photo = headers.getRequestHeader("photo").get(0);
        String bio = headers.getRequestHeader("bio").get(0);

        boolean isAdmin = false;
//        if(!isAdminString.equals(null) && isAdminString.equals("true")){
//            isAdmin = true;
//        }

        Integer iod = null;
//        if(!oidString.equals(null)){
//            iod = Integer.parseInt(oidString);
//        }

        String photo = null;
//        if(photo.equals(null)){
//            photo = null;
//        }

        ManageUser manager = new ManageUser();
        User userResult = manager.signupUser(email, username, password, isAdmin, iod, photo, bio);

        if(userResult == null){
            return Response.ok()
                    .entity("Created user : false")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        } else {
            JSONObject userJson = GiveItForwardJSON.writeUserToJSON(userResult);
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

        String dUid = headers.getRequestHeader("Uid").get(0);
        List<Request> requests = manager.getRequestsFilterByDonateUid(dUid);

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @GET
    @Path("/requests/requestuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeedFilterByRequestUid(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();
        String rUid = headers.getRequestHeader("Uid").get(0);
        List<Request> requests = manager.getRequestsFilterByRequestUid(rUid);

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.ok()
                .entity(requestJSON.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }
}
