package clearsolutions.com.javatestassignment.model;

import clearsolutions.com.javatestassignment.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testNormalUserEntity() {
        User user = User.builder()
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("123 Main St")
            .phoneNumber("123-456-7890")
            .build();

        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(foundUser.get().getAddress()).isEqualTo("123 Main St");
        assertThat(foundUser.get().getPhoneNumber()).isEqualTo("123-456-7890");
    }

    @Test
    public void testUserWithoutEmail(){
        User userWithoutEmail = User.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(userWithoutEmail);
        });
    }

    @Test
    public void testUserWithoutFirstName(){
        User userWithoutFirstName = User.builder()
                .email("test@example.com")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(userWithoutFirstName);
        });
    }

    @Test
    public void testUserWithoutLastName(){
        User userWithoutLastName = User.builder()
                .email("test@example.com")
                .firstName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(userWithoutLastName);
        });
    }

    @Test
    public void testUserWithoutBirthDate(){
        User userWithoutBirthDate = User.builder()
                .email("test@example.com")
                .firstName("Doe")
                .lastName("Doe")
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(userWithoutBirthDate);
        });
    }

    @Test
    public void testUserWithFutureBirthDate(){
        User userWithFutureBirthDate = User.builder()
                .email("test@example.com")
                .firstName("Doe")
                .lastName("Doe")
                .birthDate(LocalDate.of(2034,10,11))
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(userWithFutureBirthDate);
        });
    }

    @Test
    public void testUserEntityWithoutAddress() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("123-456-7890")
                .build();

        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(foundUser.get().getAddress()).isEqualTo(null);
        assertThat(foundUser.get().getPhoneNumber()).isEqualTo("123-456-7890");
    }

    @Test
    public void testUserEntityWithoutPhoneNumber() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Kharkiv")
                .build();

        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(foundUser.get().getAddress()).isEqualTo("Kharkiv");
        assertThat(foundUser.get().getPhoneNumber()).isEqualTo(null);
    }
}