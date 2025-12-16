package fay.dto.tag;

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
public class ResponseTag {

    private List<Tag> tags;

    public ResponseTag() {
        this.tags = new ArrayList<>();
    }
}
