package fay.resource;

import fay.dto.authentication.UpdateUserBody;
import fay.dto.authentication.UserResponse;
import fay.utils.UserUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    UserUtils util;

    @GET
    @PermitAll
    @Path("/{id}")
    public RestResponse<UserResponse> getUser(@PathParam("id") String id) {
        UserResponse user = util.getUser(id);
        if (user == null)
            return RestResponse.notFound();
        return RestResponse.ok(user);
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({"User"})
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<UserResponse> updateUser (@PathParam("id") String userId, UpdateUserBody body) {
        if (jwt.getClaim("sub").equals(userId)) {
            UserResponse response = util.updateUser(userId, body);
            response.setToken(jwt.getRawToken());

            return RestResponse.ok(response);
        } else {
            log.error("user does not have authority to change information");
        }
        return RestResponse.status(Response.Status.UNAUTHORIZED);
    }

}
