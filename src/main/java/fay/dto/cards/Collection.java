package fay.dto.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

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
    private boolean editable;
    private List<CardCollection> cards;

}
