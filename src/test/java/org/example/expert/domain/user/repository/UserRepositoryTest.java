package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 이메일로_사용자를_조회할_수_있다() {
        // given
        String email = "email@email.com";
        User user = new User(email,"password", UserRole.USER);
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByEmail(email).orElse(null);

        // then
        assertNotNull(foundUser);
        assertEquals(email,foundUser.getEmail());
        assertEquals(UserRole.USER,foundUser.getUserRole());
    }

    @Test
    void 이메일로_사용자의_존재여부를_확인할_수_있다() {
        // given
        String email = "email@email.com";
        User user = new User(email,"password", UserRole.USER);
        userRepository.save(user);

        // when
        boolean foundUser = userRepository.existsByEmail(email);

        // then
        assertTrue(foundUser);
    }
}
