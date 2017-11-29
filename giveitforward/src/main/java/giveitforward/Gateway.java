package giveitforward;

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
}
