package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;
    private UserController userController;

    @Before
    public void setUp() {
        userController = Mockito.mock(UserController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setLogin("testLogin");
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.now().minusYears(20));

        Mockito.when(userController.addUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .content("{\"id\":1,\"login\":\"testLogin\",\"name\":\"testName\"," +
                                "\"email\":\"test@test.com\",\"birthday\":\"2004-05-23\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void testGetUsers() throws Exception {
        User user = new User();
        user.setId(1);
        user.setLogin("testLogin");
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.now().minusYears(20));

        Mockito.when(userController.getUsers().values()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setLogin("testLogin");
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.now().minusYears(20));

        Mockito.when(userController.updateUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .content("{\"id\":1,\"login\":\"testLogin\",\"name\":\"testName\"," +
                                "\"email\":\"test@test.com\",\"birthday\":\"2004-05-23\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }
}
