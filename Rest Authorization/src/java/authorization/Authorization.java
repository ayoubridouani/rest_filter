package authorization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("auth")
public class Authorization {
    @GET
    @Path("/add/{a}/{b}")
    public int add(@PathParam("a") int a, @PathParam("b") int b){
        return a+b;
    }
}
