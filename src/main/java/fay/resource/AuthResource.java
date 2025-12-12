package fay.resource;

import fay.dto.authentication.CreateUserBody;
import fay.dto.authentication.LoginBody;
import fay.dto.authentication.LoginResponse;
import fay.dto.authentication.UserResponse;
import fay.utils.AuthenticationUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("/authentication")
public class AuthResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    AuthenticationUtils util;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<LoginResponse> doLogin (LoginBody body) {
        LoginResponse response = new LoginResponse();

        String validateAndToken = util.validateUserAndGenerateToken(body);
        if (validateAndToken == null) {
            response.setError("Wrong username or password");
            return RestResponse.status(RestResponse.Status.UNAUTHORIZED, response);
        }

        response.setToken(validateAndToken);
        return RestResponse.ok(response);
    }

    @GET
    @Path("me")
    @RolesAllowed({"User"})
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<UserResponse> getUser() {
        return util.getLoggedUser(jwt);
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<UserResponse> createAccount (CreateUserBody body) {
        return util.createUser(body);
    }

}
