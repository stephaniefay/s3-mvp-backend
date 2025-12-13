package fay.dto.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import fay.model.auth.User;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String id;
    private String name;
    private String nickname;
    private String email;
    private String image;
    private String bio;
    private String token;

    public UserResponse() {}

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getUsername();
        this.email = user.getEmail();
        this.image = user.getAvatar();
        this.bio = user.getBio();
    }
}
