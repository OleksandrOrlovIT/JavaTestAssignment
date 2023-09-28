package clearsolutions.com.javatestassignment.service.impl;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.repository.UserRepository;
import clearsolutions.com.javatestassignment.service.UserService;
import clearsolutions.com.javatestassignment.source.PropertySourceResolver;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        int minAge = propertyClass.getMinAge();
        if(LocalDate.now().minusYears(minAge).isBefore(user.getBirthDate())) {
            throw new ConstraintViolationException(
                    "User must be at least " + minAge + " years old",
                    Set.of());
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
}
