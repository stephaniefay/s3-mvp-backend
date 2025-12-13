package fay.utils;

import fay.dto.authentication.UpdateUserBody;
import fay.dto.authentication.UserResponse;
import fay.dto.cards.CollecWishListResponse;
import fay.dto.cards.Collection;
import fay.dto.cards.Wishlist;
import fay.dto.user.CreateUserCWBody;
import fay.model.auth.User;
import fay.model.card.Card;
import fay.model.card.UserCollection;
import fay.repository.CollectionRepository;
import fay.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class UserUtils {

    @Inject
    UserRepository repository;

    @Inject
    CollectionRepository collectionRepository;

    public UserResponse updateUser (String id, UpdateUserBody body) {
        return repository.updateUser(id, body);
    }

    public UserResponse getUser (String id) {
        Optional<User> user = repository.getUser(id);
        return user.map(UserResponse::new).orElse(null);
    }

    public CollecWishListResponse getCollectionOrWishlistFromUser (String id, String type, boolean getPrivate) {
        List<UserCollection> collectionsOrWishlists = null;
        if (type.equals(Card.Relation.COLLECTION.getName()))
            collectionsOrWishlists = collectionRepository.fetchUserCollections(id, getPrivate);
        else if (type.equals(Card.Relation.WISHLIST.getName()))
            collectionsOrWishlists = collectionRepository.fetchUserWishlist(id);

        CollecWishListResponse response = new CollecWishListResponse(new ArrayList<>());
        if (collectionsOrWishlists == null) {
            log.warn("wrong type passed [{}]", type);
            return response;
        }

        for  (UserCollection collection : collectionsOrWishlists) {
            String collectionType = collection.getType();
            if (collectionType.equals(Card.Relation.COLLECTION.getName()))
                response.getCollections().add(new Collection(collection, getPrivate));
            else if (collectionType.equals(Card.Relation.WISHLIST.getName()))
                response.getCollections().add(new Wishlist(collection, getPrivate, new ArrayList<>()));
        }

        return response;
    }

    public Collection createCollectionOrWishlist (String id, CreateUserCWBody body) {
        if (body.validate() && id != null)
            return collectionRepository.createCollecWish(body, id);

        return null;
    }

    public Collection updateCollectionOrWishlist (String collectionId, CreateUserCWBody body) {
        return collectionRepository.updateCollecWish(body, collectionId);
    }

}
