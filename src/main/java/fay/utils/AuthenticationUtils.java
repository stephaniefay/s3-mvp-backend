package fay.utils;

import fay.dto.authentication.CreateUserBody;
import fay.dto.authentication.LoginBody;
import fay.dto.authentication.UserResponse;
import fay.model.auth.User;
import fay.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class AuthenticationUtils {

    @Inject
    UserRepository repository;

    public RestResponse<UserResponse> getLoggedUser (JsonWebToken token) {
        Optional<User> userOptional = repository.getUser(token.getClaim("sub"));
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setNickname(user.getUsername());
            response.setEmail(user.getEmail());
            response.setImage(user.getAvatar());
            response.setToken(token.getRawToken());

            return RestResponse.ok(response);
        }

        return RestResponse.notFound();
    }

    public RestResponse<UserResponse> createUser(CreateUserBody body) {
        if (body.validate()) {
            log.error("passed body is invalid {}", body);
            return RestResponse.status(RestResponse.Status.BAD_REQUEST);
        }

        return RestResponse.status(RestResponse.Status.CREATED, repository.createUser(body));
    }

    public String validateUserAndGenerateToken(LoginBody body) {
        Optional<User> user = repository.getUserByUsername(body.getUsername());

        if (user.isPresent() && user.get().getPassword().equals(repository.digestPassword(body.getPassword()))) {
            log.info("User is valid, generating token");
            return generateToken(user.get());
        }

        return null;
    }

    private String generateToken (User user) {
        return Jwt.issuer("https://fay.com/issuer")
                .groups(new HashSet<>(List.of("User")))
                .claim(Claims.sub.name(), user.getId())
                .sign();
    }

}
