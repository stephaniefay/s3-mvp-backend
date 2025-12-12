package fay.dto.authentication;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class LoginBody {

    private String username;
    private String password;

}
