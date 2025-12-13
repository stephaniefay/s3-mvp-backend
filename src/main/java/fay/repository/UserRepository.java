package fay.repository;

import fay.dto.authentication.CreateUserBody;
import fay.dto.authentication.UpdateUserBody;
import fay.dto.authentication.UserResponse;
import fay.model.auth.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager eM;

    public Optional<User> getUser (String id) {
        try {
            return Optional.of(eM.find(User.class, id));
        } catch (NoResultException e) {
            log.warn("no user with id {}", id);
        }

        return Optional.empty();
    }

    public Optional<User> getUserByUsernameOrEmail(String username) {
        CriteriaBuilder criteriaBuilder = eM.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("username"), username),
                criteriaBuilder.equal(root.get("email"), username));

        criteriaQuery.where(predicate);
        try {
            return Optional.of(eM.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            log.info("User not found");
        }

        return Optional.empty();
    }

    @Transactional
    public UserResponse updateUser (String id, UpdateUserBody body) {
        Optional<User> optional = getUser(id);
        if (optional.isPresent()) {
            User user = optional.get();

            if (body.getAvatar() != null)
                user.setAvatar(body.getAvatar());

            if (body.getBio() != null)
                user.setBio(body.getBio());

            if (body.getName() != null)
                user.setName(body.getName());

            eM.merge(user);
            eM.flush();

            return new UserResponse(user);
        } else {
            return null;
        }
    }

    @Transactional
    public UserResponse createUser (CreateUserBody body) {
        User user;
        Optional<User> optional = getUserByUsernameOrEmail(body.getUsername());
        if (optional.isEmpty()) {
            user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName(body.getName());
            user.setUsername(body.getUsername());
            user.setPassword(digestPassword(body.getPassword()));
            user.setEmail(body.getEmail());

            eM.persist(user);
            eM.flush();
        } else
            user = optional.get();

        return new UserResponse(user);
    }

    public String digestPassword (String password) {
        if (password == null) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // It is crucial to specify a character encoding like UTF-8
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a Base64 encoded string for storage/transmission
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            log.error("error digesting password", e);
        }

        return null;
    }

}
