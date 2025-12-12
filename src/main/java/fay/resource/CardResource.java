package fay.resource;

import fay.dto.cards.CardCollection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Path("cards")
public class CardResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CardCollection> getCards() {
        return new ArrayList<>();
    }

}
