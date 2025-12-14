package fay.dto.cw;

import com.fasterxml.jackson.annotation.JsonInclude;
import fay.model.cw.UserCollection;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wishlist extends Collection {

    private List<String> tags;

    public Wishlist(UserCollection collection, boolean isOwner, List<String> tags) {
        super(collection, isOwner);
        this.tags = tags;
    }
}
