package fay.repository;

import fay.model.card.CardTag;
import fay.model.card.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class TagRepository {

    @Inject
    EntityManager eM;

    public List<Tag> fetchAllTags(String userId) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        Predicate predicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("userId"), userId),
                criteriaBuilder.isNull(root.get("userId")));

        criteriaQuery.where(predicate);

        try {
            return eM.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Collections.emptyList();
    }

    public void createTagRelation (List<String> ids, String cardId) {
        for (String tag : ids) {
            CardTag cardTag = new CardTag();

            cardTag.setId(UUID.randomUUID().toString());
            cardTag.setCardId(cardId);
            cardTag.setTagId(tag);

            eM.persist(cardTag);
            eM.flush();
        }
    }

    public List<String> fetchAllTagsIdsFromCard(String cardId) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<CardTag> root = criteriaQuery.from(CardTag.class);

        Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("cardId"), cardId));
        criteriaQuery.where(predicate);
        criteriaQuery.select(root.get("id"));

        try {
            return eM.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Collections.emptyList();
    }

    public List<String> fetchAllTagsFromCard (String cardId) {
        Query query = eM.createNativeQuery("select tag.name from pkmn_card_tag card, pkmn_user_tag tag where card.tagid = tag.id and card.cardid = :id");
        query.setParameter("id", cardId);


        return query.getResultList();
    }

    public void removeTagRelation (String cardId) {
        List<String> tags = fetchAllTagsIdsFromCard(cardId);

        Query query = eM.createQuery("DELETE CardTag tag WHERE tag.id IN (:ids)");
        query.setParameter("ids", tags);
        query.executeUpdate();

        eM.flush();
    }
}
