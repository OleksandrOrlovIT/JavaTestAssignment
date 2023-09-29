package clearsolutions.com.javatestassignment.service.impl;

import clearsolutions.com.javatestassignment.exception.ResourceNotFoundException;
import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.repository.UserRepository;
import clearsolutions.com.javatestassignment.source.PropertySourceResolver;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application.properties")
@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private PropertySourceResolver propertyClass;

    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, propertyClass);

        user1 = User.builder().email("test1@example.com").firstName("Magnus").lastName("Carlsen")
                .birthDate(LocalDate.of(1990, 1, 1)).address("Kharkiv")
                .build();
    }

    @Test
    void findAll() {
        User user2 = User.builder().email("test2@example.com").firstName("Johnny").lastName("Silverhand")
                .birthDate(LocalDate.of(1950, 1, 1)).address("Night City")
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> list = userService.findAll();

        assertEquals(2, list.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));

        User user = userService.findById(1L);

        assertEquals("Magnus", user.getFirstName());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdWithoutId(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(1L);
        });
    }

    @Test
    void save() {
        when(userRepository.save(any())).thenReturn(user1);

        User savedUser = userService.save(user1);

        assertEquals("Magnus", savedUser.getFirstName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void saveWithBirthDateLessThan18() {
        user1.setBirthDate(LocalDate.of(2020, 1, 1));

        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user1);
        });
    }

    @Test
    void saveWithoutBirthDate() {
        user1.setBirthDate(null);

        assertThrows(ConstraintViolationException.class, () -> {
            userService.save(user1);
        });
    }

    @Test
    void delete() {
        userService.delete(user1);

        verify(userRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        userService.deleteById(user1.getId());

        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    void updateUserPartiallyWithAllFields() {
        User foundUser = User.builder().email("test2@example.com").firstName("Johnny").lastName("Silverhand")
                .birthDate(LocalDate.of(1950, 1, 1)).address("Night City")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(foundUser));

        foundUser = userService.updateUserPartially(1L, user1);

        assertNotNull(foundUser);
        assertThat(foundUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(user1.getLastName());
        assertThat(foundUser.getBirthDate()).isEqualTo(user1.getBirthDate());
        assertThat(foundUser.getAddress()).isEqualTo(user1.getAddress());
        assertThat(foundUser.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
    }

    @Test
    void updateUserPartiallyWithSeveralFields() {
        User foundUser = User.builder().email("test2@example.com").firstName("Johnny").lastName("Silverhand")
                .birthDate(LocalDate.of(1950, 1, 1)).address("Night City")
                .build();

        user1.setBirthDate(null);
        user1.setAddress(null);
        user1.setLastName(null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(foundUser));

        foundUser = userService.updateUserPartially(1L, user1);

        assertNotNull(foundUser);
        assertThat(foundUser.getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo("Silverhand");
        assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1950, 1, 1));
        assertThat(foundUser.getAddress()).isEqualTo("Night City");
        assertThat(foundUser.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
    }

    @Test
    void updateUserPartiallyWithOneDifferentField() {
        User foundUser = User.builder().email("test2@example.com").firstName("Johnny").lastName("Silverhand")
                .birthDate(LocalDate.of(1950, 1, 1)).address("Night City")
                .build();

        User user2 = User.builder().email("user2@gmail.com").build();


        when(userRepository.findById(anyLong())).thenReturn(Optional.of(foundUser));

        foundUser = userService.updateUserPartially(1L, user2);

        assertNotNull(foundUser);
        assertThat(foundUser.getEmail()).isEqualTo(user2.getEmail());
        assertThat(foundUser.getFirstName()).isEqualTo("Johnny");
        assertThat(foundUser.getLastName()).isEqualTo("Silverhand");
        assertThat(foundUser.getBirthDate()).isEqualTo(LocalDate.of(1950, 1, 1));
        assertThat(foundUser.getAddress()).isEqualTo("Night City");
        assertThat(foundUser.getPhoneNumber()).isEqualTo(null);
    }

    @Test
    void findAllUsersInRange() {
        User user2 = User.builder().birthDate(LocalDate.of(3000, 1, 1)).build();
        User user3 = User.builder().birthDate(LocalDate.of(2000, 1, 1)).build();

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2, user3));

        LocalDate fromDate = LocalDate.of(1980, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 12, 31);

        assertEquals(2, userService.findAllUsersInRange(fromDate, toDate).size());
    }

    @Test
    void findAllUsersInRangeFromLessThanTo() {
        LocalDate fromDate = LocalDate.of(3000, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 12, 31);

        assertThrows(IllegalArgumentException.class, () -> userService.findAllUsersInRange(fromDate, toDate));
    }
}