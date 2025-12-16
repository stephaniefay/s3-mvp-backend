package fay.utils;

import fay.dto.cards.CardCollection;
import fay.model.cw.UserCollection;
import fay.repository.CWRepository;
import fay.repository.CardRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Optional;

@Slf4j
@ApplicationScoped
public class CardUtils {

    @Inject
    CardRepository cardRepository;

    @Inject
    CWRepository cwRepository;

    public RestResponse<Object> insertCardIntoCW (CardCollection card, String cwId, String type, String userId) {
        Optional<UserCollection> cw = cwRepository.findById(cwId);
        if (cw.isEmpty())
            return RestResponse.notModified();

        UserCollection userCollection = cw.get();
        if (!userCollection.getUserId().equals(userId))
            return RestResponse.status(RestResponse.Status.FORBIDDEN);

        try {
            cardRepository.addCardToCW(card, cwId, type, userId);
        } catch (Exception e) {
            log.error(e.getMessage());

            return RestResponse.notModified();
        }

        return RestResponse.status(RestResponse.Status.CREATED);
    }

    public RestResponse<Object> removeCardFromCW (String cwId, String cardId, String userId) {
        Optional<UserCollection> cw = cwRepository.findById(cwId);
        if (cw.isEmpty())
            return RestResponse.notModified();

        UserCollection userCollection = cw.get();
        if (!userCollection.getUserId().equals(userId))
            return RestResponse.status(RestResponse.Status.FORBIDDEN);

        try {
            cardRepository.removeCardFromCW(cardId);
        } catch (Exception e) {
            log.error(e.getMessage());

            return RestResponse.notModified();
        }

        return RestResponse.ok();
    }
}
