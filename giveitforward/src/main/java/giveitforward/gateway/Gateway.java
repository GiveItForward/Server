package giveitforward.gateway;

import giveitforward.managers.ManageRequest;
import giveitforward.managers.ManageUser;
import giveitforward.models.Request;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/")
public class Gateway
{
    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test()
    {

        return Response.status(200).entity("It really works!").build();
    }


    @GET
    @Path("/login/{un}/{pw}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response userLogin(@PathParam("un") String un, @PathParam("pw") String pw, @Context HttpHeaders headers)
    {

//        String username = headers.getRequestHeader("username").get(0);
//        String password = headers.getRequestHeader("password").get(0);

        ManageUser manager = new ManageUser();
        boolean result = manager.loginUser(un, pw);
        return Response.status(200).entity(un + " : " + pw + " : " + result).build();
    }

    @GET
    @Path("/signup")
    @Produces(MediaType.TEXT_PLAIN)
    public Response newUser(@Context HttpHeaders headers)
    {

        String username = headers.getRequestHeader("username").get(0);
        String password = headers.getRequestHeader("password").get(0);
        String email = headers.getRequestHeader("email").get(0);
        boolean isAdmin = Boolean.parseBoolean(headers.getRequestHeader("isAdmin").get(0));
        int iod = Integer.parseInt(headers.getRequestHeader("iod").get(0));
        String photo = headers.getRequestHeader("photo").get(0);
        String bio = headers.getRequestHeader("bio").get(0);

        ManageUser manager = new ManageUser();
        boolean result = manager.signupUser(email, username, password, isAdmin, iod, photo, bio);
        return Response.status(200).entity("Result of creating user : " + result).build();
    }


    @GET
    @Path("/home")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRequestFeed(@Context HttpHeaders headers)
    {

        String username = headers.getRequestHeader("username").get(0);

        ManageRequest manager = new ManageRequest();
        List<Request> requests = manager.getAllRequests();
        JSONArray requestJSON = GiveItForwardJSON.getRequestJSONCollection(requests);

        return Response.status(200).entity(requestJSON).build();
    }
}
