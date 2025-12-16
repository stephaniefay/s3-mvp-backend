package fay.resource;

import fay.dto.cards.CardCollection;
import fay.dto.cards.ResponseCards;
import fay.dto.cw.CollectWishResponse;
import fay.dto.cw.CollectWish;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
import fay.utils.CWUtils;
import fay.utils.CardUtils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

@Slf4j
@Path("cw")
public class CWResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    CWUtils util;

    @Inject
    CardUtils cardutils;

    @GET
    @Path("/{id}")
    public RestResponse<CollectWish> getCW (@PathParam("id") String id) {
        return util.fetchCWById(jwt.getClaim("sub"), id);
    }

    @GET
    @Path("/collections")
    public RestResponse<CollectWishResponse> getAllCollections () {
        return util.getCollections();
    }

    @GET
    @Path("/collections/{id}")
    public RestResponse<CollectWish> getCollectionById (@PathParam("id") String id) {
        return util.fetchCWById(jwt.getClaim("sub"), id);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/collections/{id}")
    public RestResponse<CollectWish> updateCollection (@PathParam("id") String id, CreateUserCWBody body) {
        return util.updateCW(jwt.getClaim("sub"), id, body);
    }

    @GET
    @Path("/wishlists")
    public RestResponse<CollectWishResponse> getAllWishlists (@QueryParam("page") Integer page, @QueryParam("size")  Integer size) {
        return util.getWishlists();
    }

    @GET
    @Path("/wishlists/{id}")
    public RestResponse<CollectWish> getWishlistById (@PathParam("id") String id) {
        return util.fetchCWById(jwt.getClaim("sub"), id);
    }

    @PATCH
    @RolesAllowed({"User"})
    @Path("/wishlists/{id}")
    public RestResponse<CollectWish> updateWishlist (@PathParam("id") String id, CreateUserCWBody body) {
        return util.updateCW(jwt.getClaim("sub"), id, body);
    }

    @GET
    @Path("/{id}/cards")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<ResponseCards> getCardsFromCW(@PathParam("id") String cwId) {
        String userId = null;
        if (jwt != null &&  jwt.getClaim("sub") != null) {
            userId = jwt.getClaim("sub");
        }

        ResponseCards response = util.fetchCardsFromCW(cwId, userId);
        if (response == null || response.getCards().isEmpty()) {
            return RestResponse.notFound();
        }

        return RestResponse.ok(response);
    }

    @PUT
    @RolesAllowed({"User"})
    @Path("/{id}/cards/add")
    public RestResponse<Object> addCardToCW (@PathParam("id") String id, @RestQuery("type") String type, CardCollection body) {
        return cardutils.insertCardIntoCW(body, id, type != null ? type : Card.Relation.COLLECTION.getName(), jwt.getClaim("sub"));
    }

    @DELETE
    @RolesAllowed({"User"})
    @Path("/{id}/cards/remove/{card}")
    public RestResponse<Object> removeCardFromCW (@PathParam("id") String id, @PathParam("card") String cardId) {
        return cardutils.removeCardFromCW(id, cardId, jwt.getClaim("sub"));
    }

}
