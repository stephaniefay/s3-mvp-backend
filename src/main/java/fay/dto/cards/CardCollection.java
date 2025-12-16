package fay.dto.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import fay.model.card.Card;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardCollection {

    private String id;
    private String externalId;
    private String name;
    private String image;
    private Integer condition;
    private String language;
    private Integer priority;
    private List<String> tags;

    public CardCollection() {
    }

    public CardCollection(Card card, List<String> cardTags) {
        this.id = card.getId();
        this.externalId = card.getExternalId();
        this.name = card.getName();
        this.image = card.getImage();
        this.condition = card.getCondition();
        this.language = card.getLanguage();
        this.priority = card.getPriority();

        if (cardTags != null && !cardTags.isEmpty()) {
            tags = new ArrayList<>();
            tags.addAll(cardTags);
        }
    }
}
