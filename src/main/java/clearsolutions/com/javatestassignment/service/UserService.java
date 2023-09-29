package clearsolutions.com.javatestassignment.service;

import clearsolutions.com.javatestassignment.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    void delete(User user);

    void deleteById(Long id);

    User updateUserPartially(Long searchUserId, User newUser);
    List<User> findAllUsersInRange(LocalDate from, LocalDate to);
}
