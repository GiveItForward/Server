package giveitforward.gateway;

import giveitforward.managers.ManageRequest;
import giveitforward.managers.ManageUser;
import giveitforward.models.Request;
import giveitforward.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@Context HttpHeaders headers) // @PathParam("un") String un, @PathParam("pw") String pw
    {

        String username = headers.getRequestHeader("username").get(0);
        String password = headers.getRequestHeader("password").get(0);

        ManageUser manager = new ManageUser();
        User userResult = manager.loginUser(username, password);

        if (userResult != null)
        {
            JSONObject jsonUser = GiveItForwardJSON.writeUserToJSON(userResult);
            return Response.status(200).entity("Logged in user : true\n\n" + jsonUser).build();
        } else
        {
            return Response.status(200).entity("Logged in user : false").build();
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
            return Response.status(200).entity("Created user : false").build();
        } else {
            JSONObject userJson = GiveItForwardJSON.writeUserToJSON(userResult);
            return Response.status(200).entity("Result of creating user true\n\n: " + userJson).build();
        }

    }


    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequestFeed(@Context HttpHeaders headers)
    {

        ManageRequest manager = new ManageRequest();
        List<Request> requests = manager.getAllRequests();

        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.status(200).entity(requestJSON.toString()).build();
    }
}
