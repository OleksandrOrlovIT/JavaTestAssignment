package clearsolutions.com.javatestassignment.service.impl;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.repository.UserRepository;
import clearsolutions.com.javatestassignment.source.PropertySourceResolver;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void save() {
        PropertySourceResolver propertyClass = new PropertySourceResolver();
        when(userRepository.save(any())).thenReturn(user1);

        User savedUser = userService.save(user1);

        assertEquals("Magnus", savedUser.getFirstName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void saveWithBirthDateLessThan18() {
        PropertySourceResolver propertyClass = new PropertySourceResolver();
        user1.setBirthDate(LocalDate.of(2020, 1, 1));

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
}