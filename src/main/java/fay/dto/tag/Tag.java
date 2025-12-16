package fay.dto.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag {

    private String id;
    private String name;

    public Tag() {
    }

    public Tag(fay.model.card.Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
