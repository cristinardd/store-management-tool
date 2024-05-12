package storemanagementtool.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import storemanagementtool.store.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}