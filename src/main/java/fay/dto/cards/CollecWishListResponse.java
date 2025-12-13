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
public class CollecWishListResponse {

    List<Collection> collections;

    public CollecWishListResponse(List<Collection> collections) {
        this.collections = collections;
    }
}
