package fay.utils;

import fay.dto.Paginator;
import fay.dto.cw.CollectWishResponse;
import fay.dto.cw.Collection;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
import fay.model.cw.UserCollection;
import fay.repository.CWRepository;
import fay.repository.CardRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class CWUtils {

    @Inject
    CWRepository repository;

    @Inject
    CardRepository cardRepository;

    public RestResponse<Collection> updateCollectionOrWishlist (String userId, String collectionId, CreateUserCWBody body) {
        Optional<UserCollection> optional = repository.findById(collectionId);

        if (optional.isEmpty()) {
            log.error("CW Not Found");
            return RestResponse.notFound();
        } else {
            UserCollection collection = optional.get();
            if (!collection.getUserId().equals(userId)) {
                log.warn("User {} trying to modify cw {}, from user {}", userId, collectionId, collection.getUserId());
                return RestResponse.status(RestResponse.Status.FORBIDDEN);
            }

            Collection updatedCW = repository.updateCW(body, collection);
            return RestResponse.ok(updatedCW);
        }
    }

    public RestResponse<Collection> fetchCWById (String userId, String collectionId) {
        Optional<UserCollection> optional = repository.findById(collectionId);
        if (optional.isEmpty()) {
            log.error("CW not found {}", collectionId);
            return RestResponse.notFound();
        } else {
            UserCollection collection = optional.get();
            if (!collection.getUserId().equals(userId) && "true".equals(collection.getIsPrivate())) {
                return RestResponse.notFound();
            }

            return RestResponse.ok(new Collection(collection, collection.getUserId().equals(userId)));
        }
    }

    public RestResponse<CollectWishResponse> getCollections (int page, int size) {
        List<UserCollection> collections = repository.fetchAllCWPaginated(new Paginator(page, size), Card.Relation.COLLECTION.getName());

        CollectWishResponse response = new CollectWishResponse(new ArrayList<>());
        for (UserCollection collection : collections) {
            response.getCw().add(new Collection(collection, false));
        }

        return RestResponse.ok(response);
    }

    public RestResponse<CollectWishResponse> getWishlists (int page, int size) {
        List<UserCollection> collections = repository.fetchAllCWPaginated(new Paginator(page, size), Card.Relation.WISHLIST.getName());

        CollectWishResponse response = new CollectWishResponse(new ArrayList<>());
        for (UserCollection collection : collections) {
            response.getCw().add(new Collection(collection, false));
        }

        return RestResponse.ok(response);
    }
}
