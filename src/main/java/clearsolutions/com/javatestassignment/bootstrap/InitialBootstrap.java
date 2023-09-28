package clearsolutions.com.javatestassignment.bootstrap;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InitialBootstrap implements CommandLineRunner {

    private final UserService userService;

    public InitialBootstrap(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder().email("test1@example.com").firstName("Magnus").lastName("Carlsen")
                .birthDate(LocalDate.of(1990, 1, 1)).address("Kharkiv").phoneNumber("111-111-1111")
                .build();

        User user2 = User.builder().email("test2@example.com").firstName("Johnny").lastName("Silverhand")
                .birthDate(LocalDate.of(1950, 1, 1)).address("Night City").phoneNumber("222-222-2222")
                .build();

        User user3 = User.builder().email("test3@example.com").firstName("Satoru").lastName("Gojo")
                .birthDate(LocalDate.of(2000, 1, 1)).address("Tokyo").phoneNumber("333-333-3333")
                .build();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
    }
}
