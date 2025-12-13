package fay.resource;

import fay.dto.authentication.UpdateUserBody;
import fay.dto.authentication.UserResponse;
import fay.dto.cards.CollecWishListResponse;
import fay.dto.cards.Collection;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
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

    @PATCH
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

    @GET
    @Path("/{id}/collections")
    public RestResponse<CollecWishListResponse> getCollectionsFromUser (@PathParam("id") String id) {
        boolean getPrivate = id.equals(jwt.getClaim("sub"));
        return RestResponse.ok(util.getCollectionOrWishlistFromUser(id, Card.Relation.COLLECTION.getName(), getPrivate));
    }

    @POST
    @RolesAllowed({"User"})
    @Path("/collections")
    public RestResponse<Collection> createCollection (CreateUserCWBody body) {
        Collection created = util.createCollectionOrWishlist(jwt.getClaim("sub"), body);

        if (created != null)
            return RestResponse.status(Response.Status.CREATED, created);
        return  RestResponse.status(RestResponse.Status.BAD_REQUEST);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/{userId}/collections/{id}")
    public RestResponse<Collection> updateCollection (@PathParam("userId") String id, @PathParam("id") String collectionId, CreateUserCWBody body) {
        if (jwt.getClaim("sub").equals(id)) {
            Collection response = util.updateCollectionOrWishlist(collectionId, body);
            if (response != null)
                return RestResponse.ok(response);
            return RestResponse.notModified();
        } else {
            log.error("user does not have authority to change information");
        }
        return RestResponse.status(Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("/{id}/wishlists")
    public RestResponse<CollecWishListResponse> getWishlistsFromUser (@PathParam("id") String id) {
        boolean getPrivate = id.equals(jwt.getClaim("sub"));
        return RestResponse.ok(util.getCollectionOrWishlistFromUser(id, Card.Relation.WISHLIST.getName(), getPrivate));
    }

    @POST
    @RolesAllowed({"User"})
    @Path("/wishlists")
    public RestResponse<Collection> createWishlist (CreateUserCWBody body) {
        Collection created = util.createCollectionOrWishlist(jwt.getClaim("sub"), body);

        if (created != null)
            return RestResponse.status(Response.Status.CREATED, created);
        return  RestResponse.status(RestResponse.Status.BAD_REQUEST);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/{userId}/wishlists/{id}")
    public RestResponse<Collection> updateWishlist (@PathParam("userId") String id, @PathParam("id") String wishlistId, CreateUserCWBody body) {
        if (jwt.getClaim("sub").equals(id)) {
            Collection response = util.updateCollectionOrWishlist(wishlistId, body);
            if (response != null)
                return RestResponse.ok(response);
            return RestResponse.notModified();
        } else {
            log.error("user does not have authority to change information");
        }
        return RestResponse.status(Response.Status.UNAUTHORIZED);
    }

}
