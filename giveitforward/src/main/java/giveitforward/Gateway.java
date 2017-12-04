package giveitforward;

import giveitforwardtests.ManageUser;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/")
public class Gateway {
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

        boolean result = ManageUser.loginUser(username, password);
        return Response.status(200).entity(username + " : " + password + " : " + result).build();
    }
}
