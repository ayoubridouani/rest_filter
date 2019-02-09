package authorization;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;

@Provider
public class SecurityFilter implements ContainerRequestFilter {	
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization"; 
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic "; 
    private static final String SECURED_URL_PREFIX = "auth"; 

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            if (authHeader != null && authHeader.size() > 0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                byte[] valueDecoded;
                try {
                    valueDecoded = Base64.decode(authToken);
                    String decodedString = new String(valueDecoded);
                    StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                    String username = tokenizer.nextToken();
                    String password = tokenizer.nextToken();

                    if ("user".equals(username) && "password".equals(password)) {
                            return;
                    }
                } catch (Base64DecodingException ex) {}
            }
            Response unauthorizedStatus = Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build();

            requestContext.abortWith(unauthorizedStatus);
        }
        
    }
}
