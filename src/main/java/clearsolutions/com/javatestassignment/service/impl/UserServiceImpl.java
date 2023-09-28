package clearsolutions.com.javatestassignment.service.impl;

import clearsolutions.com.javatestassignment.exception.ResourceNotFoundException;
import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.repository.UserRepository;
import clearsolutions.com.javatestassignment.service.UserService;
import clearsolutions.com.javatestassignment.source.PropertySourceResolver;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PropertySourceResolver propertyClass;

    public UserServiceImpl(UserRepository userRepository, PropertySourceResolver propertyClass) {
        this.userRepository = userRepository;
        this.propertyClass = propertyClass;
    }

    @Override
    public List<User> findAll() {
            return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null)
            return userRepository.findById(id).orElse(null);
        else
            throw new ResourceNotFoundException("User doesn't exist with id: " + id);
    }

    @Override
    public User save(User user) {
        int minAge = propertyClass.getMinAge();
        if(user.getBirthDate() == null || LocalDate.now().minusYears(minAge).isBefore(user.getBirthDate())) {
            throw new ConstraintViolationException("User must be at least " + minAge + " years old", Set.of());
        }
        else
            return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllUsersInRange(LocalDate from, LocalDate to) {
        return findAll().stream()
                .filter(user -> user.getBirthDate().isAfter(from) && user.getBirthDate().isBefore(to))
                .collect(Collectors.toList());
    }
}
