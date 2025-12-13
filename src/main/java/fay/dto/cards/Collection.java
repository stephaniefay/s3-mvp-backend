package fay.dto.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import fay.model.card.UserCollection;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection {

    private String id;
    private String name;
    private String description;
    private String cover;
    private boolean isPrivate;
    private boolean editable;
    private List<CardCollection> cards;

    public Collection(UserCollection collection, boolean isOwner) {
        this.id = collection.getId();
        this.name = collection.getName();
        this.description = collection.getDescription();
        this.cover = collection.getCover();
        this.isPrivate = collection.getIsPrivate().equals("true");
        this.editable = isOwner;
        this.cards = new ArrayList<>();
    }
}
