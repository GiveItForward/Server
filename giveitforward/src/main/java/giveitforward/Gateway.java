package giveitforward;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/")
public class Gateway {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;


    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {

        return Response.status(200).entity("It really works!").build();
    }

    @GET
    @Path("/login/{param1}/{param2}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response newUser(@PathParam("param1") String username, @PathParam("param2") String password) {

        ManageUser.loginUser(username, password);
        return Response.status(200).entity(username + " : " + password).build();
    }
}
