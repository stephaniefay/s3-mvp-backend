package fay.resource;

import fay.dto.cw.CollectWishResponse;
import fay.dto.cw.Collection;
import fay.dto.user.CreateUserCWBody;
import fay.utils.CWUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("cw")
public class CWResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    CWUtils util;

    @GET
    @Path("/collections")
    public RestResponse<CollectWishResponse> getAllCollections (@QueryParam("page") Integer page, @QueryParam("size")  Integer size) {
        return util.getCollections(page, size);
    }

    @GET
    @Path("/collections/{id}")
    public RestResponse<Collection> getCollectionById (@PathParam("id") String id) {
        return util.fetchCWById(jwt.getClaim("sub"), id);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/collections/{id}")
    public RestResponse<Collection> updateCollection (@PathParam("id") String id, CreateUserCWBody body) {
        return util.updateCollectionOrWishlist(jwt.getClaim("sub"), id, body);
    }

    @GET
    @Path("/wishlists")
    public RestResponse<CollectWishResponse> getAllWishlists (@QueryParam("page") Integer page, @QueryParam("size")  Integer size) {
        return util.getWishlists(page, size);
    }

    @GET
    @Path("/wishlists/{id}")
    public RestResponse<Collection> getWishlistById (@PathParam("id") String id) {
        return util.fetchCWById(jwt.getClaim("sub"), id);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/wishlists/{id}")
    public RestResponse<Collection> updateWishlist (@PathParam("id") String id, CreateUserCWBody body) {
        return util.updateCollectionOrWishlist(jwt.getClaim("sub"), id, body);
    }

}
