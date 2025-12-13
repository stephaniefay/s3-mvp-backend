package fay.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserCWBody {

    private String name;
    private String description;
    private Boolean isPrivate;
    private String cover;
    private String type;

    public boolean validate () {
        return name != null && !name.isBlank() && type != null && !type.isBlank();
    }

}
