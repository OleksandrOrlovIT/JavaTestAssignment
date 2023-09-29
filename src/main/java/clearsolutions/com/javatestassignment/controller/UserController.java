package clearsolutions.com.javatestassignment.controller;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> retrieveUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        user.setId(null);
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if(userService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        if(userService.findById(id) == null)
            return ResponseEntity.notFound().build();
        user.setId(id);
        if(userService.save(user) == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable Long id, @RequestBody User user) {
        User foundUser = userService.updateUserPartially(id, user);
        if(foundUser == null)
            return ResponseEntity.notFound().build();
        if(userService.save(foundUser) == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/users/{from}/{to}")
    public List<User> retrieveUsersInRange(@PathVariable LocalDate from, @PathVariable LocalDate to) {
        return userService.findAllUsersInRange(from, to);
    }
}
