package fay.repository;

import fay.dto.cards.Collection;
import fay.dto.cards.Wishlist;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
import fay.model.card.UserCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@ApplicationScoped
public class CollectionRepository {

    @Inject
    EntityManager eM;

    public Optional<UserCollection> findById(String id) {
        try {
            return Optional.of(eM.find(UserCollection.class, id));
        } catch (NoResultException e) {
            log.error("no user collection with id {}", id);
        }

        return Optional.empty();
    }

    @Transactional
    public Collection createCollecWish(CreateUserCWBody body, String userId) {
        UserCollection userCollection = new UserCollection();
        userCollection.setId(UUID.randomUUID().toString());
        userCollection.setUserId(userId);
        userCollection.setName(body.getName());
        userCollection.setCover(body.getCover());
        userCollection.setDescription(body.getDescription());
        userCollection.setIsPrivate(body.getIsPrivate() ? "true"  : "false");
        userCollection.setType(body.getType());

        eM.persist(userCollection);
        eM.flush();

        return body.getType().equals(Card.Relation.COLLECTION.getName()) ? new Collection(userCollection, true) :
                new Wishlist(userCollection, true, new ArrayList<>());
    }

    @Transactional
    public Collection updateCollecWish(CreateUserCWBody body, String collectionId) {
        Optional<UserCollection> optional = findById(collectionId);
        if (optional.isPresent()) {
            UserCollection userCollection = optional.get();

            if (body.getName() != null) {
                userCollection.setName(body.getName());
            }

            if (body.getDescription() != null) {
                userCollection.setDescription(body.getDescription());
            }

            if (body.getCover() != null) {
                userCollection.setCover(body.getCover());
            }

            if (body.getIsPrivate() != null) {
                userCollection.setIsPrivate(body.getIsPrivate() ? "true"  : "false");
            }

            eM.merge(userCollection);
            eM.flush();

            return new Collection(userCollection, true);
        }

        return null;
    }

    public List<UserCollection> fetchUserCollections(String id, boolean getPrivate) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<UserCollection> criteriaQuery = criteriaBuilder.createQuery(UserCollection.class);
        Root<UserCollection> root = criteriaQuery.from(UserCollection.class);

        Predicate predicate = criteriaBuilder.equal(root.get("userId"), id);
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), Card.Relation.COLLECTION.getName()));
        if (!getPrivate)
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isPrivate"), "false"));

        criteriaQuery.where(predicate);

        try {
            return eM.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<UserCollection> fetchUserWishlist(String id) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<UserCollection> criteriaQuery = criteriaBuilder.createQuery(UserCollection.class);
        Root<UserCollection> root = criteriaQuery.from(UserCollection.class);

        Predicate predicate = criteriaBuilder.equal(root.get("userId"), id);
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), Card.Relation.WISHLIST.getName()));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isPrivate"), "false"));

        criteriaQuery.where(predicate);

        try {
            return eM.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

}
