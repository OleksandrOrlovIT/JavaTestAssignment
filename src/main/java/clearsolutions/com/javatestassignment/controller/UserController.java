package clearsolutions.com.javatestassignment.controller;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> retrieveUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> retrieveUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null)
            return null;
        return EntityModel.of(user);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        user.setId(null);
        User savedUser = userService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        userService.findById(id);
        user.setId(id);
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable Long id, @RequestBody User user) throws IllegalAccessException, NoSuchFieldException {
        User foundUser = userService.findById(id);
        for (Field field : user.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(user);

            if (null != value) {
                Field destField = foundUser.getClass().getDeclaredField(name);
                destField.setAccessible(true);
                destField.set(foundUser, value);
            }
        }
        userService.save(foundUser);
        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/users/{from}/{to}")
    public List<User> retrieveUsers(@PathVariable LocalDate from, @PathVariable LocalDate to) {
        if(from.isAfter(to))
            throw new IllegalArgumentException(from + " is after " + to);
        else
            return userService.findAllUsersInRange(from, to);
    }
}
