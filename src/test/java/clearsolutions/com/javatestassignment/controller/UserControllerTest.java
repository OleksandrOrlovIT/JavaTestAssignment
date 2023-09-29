package clearsolutions.com.javatestassignment.controller;

import clearsolutions.com.javatestassignment.model.User;
import clearsolutions.com.javatestassignment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.annotations.ManyToAny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User user1;

    private Long userId;

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = User.builder().email("test1@example.com").firstName("Magnus").lastName("Carlsen")
                .birthDate(LocalDate.of(1990, 1, 1)).address("Kharkiv")
                .build();

        userId = 1L;
    }

    @Test
    void retrieveUsers() throws Exception {
        User user2 = User.builder().build();

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void retrieveUsersWithoutAny() throws Exception {
        when(userService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void retrieveUserById() throws Exception {
        when(userService.findById(userId)).thenReturn(user1);

        mockMvc.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Magnus"));
    }

    @Test
    void retrieveUserByIdWithNoUsers() throws Exception {
        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        when(userService.save(any())).thenReturn(user1);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Magnus"))
                .andExpect(jsonPath("$.lastName").value("Carlsen"));
    }

    @Test
    void createUserWithInvalidField() throws Exception {
        user1.setBirthDate(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteById(anyLong());
        when(userService.findById(anyLong())).thenReturn(user1);

        mockMvc.perform(delete("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteById(anyLong());
        verify(userService, times(1)).findById(anyLong());
    }

    @Test
    void deleteUserWithWrongId() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(0)).deleteById(anyLong());
        verify(userService, times(1)).findById(anyLong());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.findById(anyLong())).thenReturn(user1);
        when(userService.save(any())).thenReturn(user1);

        mockMvc.perform(put("/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Magnus"))
                .andExpect(jsonPath("$.lastName").value("Carlsen"));

        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(1)).save(any());
    }

    @Test
    void updateUserWithWrongId() throws Exception {
        when(userService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(put("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(0)).save(any());
    }

    @Test
    void updateUserWithWrongJsonBody() throws Exception {
        user1.setId(1L);
        when(userService.findById(anyLong())).thenReturn(User.builder().build());

        mockMvc.perform(put("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(1)).save(any());
    }


    @Test
    void updateUserPartially() throws Exception {
        when(userService.updateUserPartially(anyLong(), any())).thenReturn(user1);
        when(userService.save(any())).thenReturn(user1);

        mockMvc.perform(patch("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Magnus"))
                .andExpect(jsonPath("$.lastName").value("Carlsen"));

        verify(userService, times(1)).updateUserPartially(anyLong(), any());
        verify(userService, times(1)).save(any());
    }

    @Test
    void updateUserPartiallyWithWrongId() throws Exception {
        mockMvc.perform(patch("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUserPartially(anyLong(), any());
        verify(userService, times(0)).save(any());
    }

    @Test
    void updateUserPartiallyWithWrongFields() throws Exception {
        user1.setBirthDate(LocalDate.of(2020, 1, 1));
        when(userService.updateUserPartially(anyLong(), any())).thenReturn(user1);

        mockMvc.perform(patch("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(user1)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).updateUserPartially(anyLong(), any());
        verify(userService, times(1)).save(any());
    }

    @Test
    void testRetrieveUsersInRange() throws Exception {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);

        when(userService.findAllUsersInRange(from, to))
                .thenReturn(Arrays.asList(user1, User.builder().firstName("Sasha").build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{from}/{to}", from, to)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Magnus"))
                .andExpect(jsonPath("$[1].firstName").value("Sasha"));

        verify(userService, times(1)).findAllUsersInRange(from, to);
    }
}