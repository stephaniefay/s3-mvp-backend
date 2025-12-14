package fay.dto.cw;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectWishResponse {

    List<Collection> cw;

    public CollectWishResponse(List<Collection> cw) {
        this.cw = cw;
    }
}
