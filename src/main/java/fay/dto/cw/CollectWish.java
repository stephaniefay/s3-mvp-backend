package fay.dto.cw;

import com.fasterxml.jackson.annotation.JsonInclude;
import fay.dto.cards.CardCollection;
import fay.model.cw.UserCollection;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectWish {

    private String id;
    private String userId;
    private String name;
    private String description;
    private String cover;
    private String privacy;
    private boolean editable;
    private String type;
    private List<CardCollection> cards;

    public CollectWish() {
    }

    public CollectWish(UserCollection collection, boolean isOwner) {
        this.id = collection.getId();
        this.userId = collection.getUserId();
        this.name = collection.getName();
        this.description = collection.getDescription();
        this.cover = collection.getCover();
        this.privacy = collection.getIsPrivate().equals("true") ? "private" : "public";
        this.editable = isOwner;
        this.type = collection.getType();
        this.cards = new ArrayList<>();
    }
}
