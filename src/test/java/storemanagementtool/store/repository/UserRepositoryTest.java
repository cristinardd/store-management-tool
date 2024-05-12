package storemanagementtool.store.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import storemanagementtool.store.model.User;
import storemanagementtool.store.model.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUserSaved_whenFindByUsername_thenUserFound() {
        User newUser = buildUser();
        userRepository.save(newUser);

        Optional<User> found = userRepository.findByUsername("user");

        assertTrue(found.isPresent());
        assertEquals("user", found.get().getUsername());
    }

    @Test
    public void givenUserNotSaved_whenFindByUsername_thenUserNotFound() {
        Optional<User> found = userRepository.findByUsername("nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    public void givenUserSaved_whenExistsByUsername_thenUserExists() {
        User newUser = buildUser();
        userRepository.save(newUser);

        boolean exists = userRepository.existsByUsername("user");

        assertTrue(exists);
    }

    @Test
    public void givenUserNotSaved_whenExistsByUsername_thenUserDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nonexistent");

        assertFalse(exists);
    }


    private User buildUser() {
        return User.builder()
                .username("user")
                .password("123")
                .role(Role.USER)
                .build();
    }
}