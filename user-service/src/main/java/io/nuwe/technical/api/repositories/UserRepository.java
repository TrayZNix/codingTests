package io.nuwe.technical.api.repositories;

import java.util.List;
import java.util.Optional;

import io.nuwe.technical.api.entities.User;

import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(long id);
    User save(User s);
    void delete(User s);
    void deleteAll();

    Optional<User> findByEmail(java.lang.String email);
}
