package fay.dto.authentication;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class UpdateUserBody {

    private String name;
    private String avatar;
    private String bio;

}
