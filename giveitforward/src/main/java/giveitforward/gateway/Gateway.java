package giveitforward.gateway;

import giveitforward.managers.ManageUser;

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
    @Path("/login/{un}/{pw}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response newUser(@PathParam("un") String un, @PathParam("pw") String pw, @Context HttpHeaders headers) {

//        String username = headers.getRequestHeader("username").get(0);
//        String password = headers.getRequestHeader("password").get(0);

        boolean result = ManageUser.loginUser(un, pw);
        return Response.status(200).entity(un + " : " + pw + " : " + result).build();
    }
}
