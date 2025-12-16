package fay.dto.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCards {

    private List<CardCollection> cards;

    public ResponseCards() {
        this.cards = new ArrayList<>();
    }
}
