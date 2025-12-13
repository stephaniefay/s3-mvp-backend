package fay.utils;

import fay.dto.authentication.UpdateUserBody;
import fay.dto.authentication.UserResponse;
import fay.model.auth.User;
import fay.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@ApplicationScoped
public class UserUtils {

    @Inject
    UserRepository repository;

    public UserResponse updateUser (String id, UpdateUserBody body) {
        return repository.updateUser(id, body);
    }

    public UserResponse getUser (String id) {
        Optional<User> user = repository.getUser(id);
        return user.map(UserResponse::new).orElse(null);
    }

}
