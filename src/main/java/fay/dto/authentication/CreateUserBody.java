package fay.dto.authentication;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class CreateUserBody {

    private String name;
    private String username;
    private String email;
    private String password;

    public boolean validate () {
        return name == null || name.isBlank() || username ==  null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank();
    }
}
