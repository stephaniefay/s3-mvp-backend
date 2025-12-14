package fay.repository;

import fay.dto.Paginator;
import fay.dto.cw.Collection;
import fay.dto.cw.Wishlist;
import fay.dto.user.CreateUserCWBody;
import fay.model.card.Card;
import fay.model.cw.UserCollection;
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
public class CWRepository {

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
    public Collection createCW(CreateUserCWBody body, String userId) {
        UserCollection userCollection = new UserCollection();
        userCollection.setId(UUID.randomUUID().toString());
        userCollection.setUserId(userId);
        userCollection.setName(body.getName());
        userCollection.setCover(body.getCover());
        userCollection.setDescription(body.getDescription());
        userCollection.setIsPrivate(body.getIsPrivate() ? "true" : "false");
        userCollection.setType(body.getType());

        eM.persist(userCollection);
        eM.flush();

        return body.getType().equals(Card.Relation.COLLECTION.getName()) ? new Collection(userCollection, true) :
                new Wishlist(userCollection, true, new ArrayList<>());
    }

    @Transactional
    public Collection updateCW(CreateUserCWBody body, UserCollection collection) {
        if (body.getName() != null) {
            collection.setName(body.getName());
        }

        if (body.getDescription() != null) {
            collection.setDescription(body.getDescription());
        }

        if (body.getCover() != null) {
            collection.setCover(body.getCover());
        }

        if (body.getIsPrivate() != null) {
            collection.setIsPrivate(body.getIsPrivate() ? "true" : "false");
        }

        eM.merge(collection);
        eM.flush();

        return new Collection(collection, true);
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

    public List<UserCollection> fetchUserWishlists(String id) {
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

    public List<UserCollection> fetchAllCWPaginated(Paginator paginator, String type) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<UserCollection> criteriaQuery = criteriaBuilder.createQuery(UserCollection.class);
        Root<UserCollection> root = criteriaQuery.from(UserCollection.class);

        Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("isPrivate"), "false"));
        predicate =  criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), type));

        criteriaQuery.where(predicate);

        try {
            return eM.createQuery(criteriaQuery).setFirstResult(paginator.getFirstIndex())
                    .setMaxResults(paginator.getSize()).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Collections.emptyList();
    }

}
