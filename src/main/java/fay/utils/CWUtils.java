package fay.utils;

import fay.dto.cards.CardCollection;
import fay.dto.cards.ResponseCards;
import fay.dto.cw.CollectWish;
import fay.dto.cw.CollectWishResponse;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
import fay.model.card.CardTag;
import fay.model.card.Tag;
import fay.model.cw.UserCollection;
import fay.repository.CWRepository;
import fay.repository.CardRepository;
import fay.repository.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @Inject
    TagRepository tagRepository;

    @Transactional
    public RestResponse<CollectWish> updateCW(String userId, String cwId, CreateUserCWBody body) {
        Optional<UserCollection> optional = repository.findById(cwId);

        if (optional.isEmpty()) {
            log.error("CW Not Found");
            return RestResponse.notFound();
        } else {
            UserCollection collection = optional.get();
            if (!collection.getUserId().equals(userId)) {
                log.warn("User {} trying to modify cw {}, from user {}", userId, cwId, collection.getUserId());
                return RestResponse.status(RestResponse.Status.FORBIDDEN);
            }

            CollectWish updatedCW = repository.updateCW(body, collection);
            return RestResponse.ok(updatedCW);
        }
    }

    public RestResponse<CollectWish> fetchCWById(String userId, String cwId) {
        Optional<UserCollection> optional = repository.findById(cwId);
        if (optional.isEmpty()) {
            log.error("CW not found {}", cwId);
            return RestResponse.notFound();
        } else {
            UserCollection collection = optional.get();
            if (!collection.getUserId().equals(userId) && "true".equals(collection.getIsPrivate())) {
                return RestResponse.notFound();
            }

            return RestResponse.ok(new CollectWish(collection, collection.getUserId().equals(userId)));
        }
    }

    public RestResponse<CollectWishResponse> getCollections() {
        List<UserCollection> collections = repository.fetchAllCW(Card.Relation.COLLECTION.getName());

        CollectWishResponse response = new CollectWishResponse(new ArrayList<>());
        for (UserCollection collection : collections) {
            response.getCw().add(new CollectWish(collection, false));
        }

        return RestResponse.ok(response);
    }

    public RestResponse<CollectWishResponse> getWishlists() {
        List<UserCollection> collections = repository.fetchAllCW(Card.Relation.WISHLIST.getName());

        CollectWishResponse response = new CollectWishResponse(new ArrayList<>());
        for (UserCollection collection : collections) {
            response.getCw().add(new CollectWish(collection, false));
        }

        return RestResponse.ok(response);
    }

    public ResponseCards fetchCardsFromCW (String cwId, String userId) {
        Optional<UserCollection> cwOptional = repository.findById(cwId);
        ResponseCards response = new ResponseCards();

        if (cwOptional.isEmpty()) {
            return response;
        } else {
            UserCollection cw = cwOptional.get();
            if (!cw.getUserId().equals(userId) && cw.getIsPrivate().equals("true")) {
                return response;
            }

            List<Card> cards = cardRepository.getAllCardsFromCW(cw.getId(), cw.getType());

            for (Card card : cards) {
                List<String> tags = tagRepository.fetchAllTagsFromCard(card.getId());
                response.getCards().add(new CardCollection(card, tags));
            }
        }

        return response;
    }
}
