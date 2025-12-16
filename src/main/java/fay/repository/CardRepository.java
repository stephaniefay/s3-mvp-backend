package fay.repository;

import fay.dto.cards.CardCollection;
import fay.model.card.Card;
import fay.model.card.CardTag;
import fay.model.card.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CardRepository {

    @Inject
    EntityManager eM;

    @Inject
    TagRepository tagRepository;

    @Transactional
    public Card addCardToCW (CardCollection temp, String cwId, String type, String userId) {
        String cardId = UUID.randomUUID().toString();

        Card card = new Card();
        card.setId(cardId);
        card.setExternalId(temp.getExternalId());
        card.setName(temp.getName());
        card.setImage(temp.getImage());
        card.setRelationId(cwId);
        card.setRelation(type);
        card.setUserId(userId);
        card.setPriority(temp.getPriority());
        card.setCondition(temp.getCondition());
        card.setLanguage(temp.getLanguage());

        if (temp.getTags() != null) {
           tagRepository.createTagRelation(temp.getTags(), cardId);
        }

        eM.persist(card);
        eM.flush();

        return card;
    }

    @Transactional
    public void removeCardFromCW (String cardId) {
        Card card = eM.find(Card.class, cardId);

        tagRepository.removeTagRelation(cardId);

        eM.remove(card);
        eM.flush();
    }

    public List<Card> getAllCardsFromCW (String cwId, String type) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<Card> criteriaQuery = criteriaBuilder.createQuery(Card.class);
        Root<Card> root = criteriaQuery.from(Card.class);

        Predicate predicate = criteriaBuilder.equal(root.get("relation"), type);
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("relationId"), cwId));

        criteriaQuery.where(predicate);

        try {
            return eM.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Collections.emptyList();
    }
}
