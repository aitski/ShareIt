package ru.yandex.practicum.ShareIt.UserTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.ShareIt.user.UserController;
import ru.yandex.practicum.ShareIt.user.UserMapper;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.model.UserDto;
import ru.yandex.practicum.ShareIt.user.service.UserServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenUserGetById_thenReturnUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        UserDto userDto = new UserDto(1, "user", "user@user.com");

        when(userService.getById(1))
                .thenReturn(user);

        when(userMapper.convertToDto(any()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@user.com")));
    }

    @Test
    public void whenUserGetAll_thenReturnUserList() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        UserDto userDto = new UserDto(1, "user", "user@user.com");

        when(userService.getAll())
                .thenReturn(List.of(user));

        when(userMapper.convertToDto(any()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("user")))
                .andExpect(jsonPath("$[0].email", is("user@user.com")));
    }

    @Test
    public void whenCreateUser_thenReturnUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");

        when(userService.create(any()))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .content("{\"name\": \"user\", \"email\":\"user@user.com\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("user@user.com")));
    }

    @Test
    public void whenPatchUser_thenReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("updated@user.com");

        UserDto userUpd = new UserDto(1L, "user", "updated@user.com");

        when(userService.update(any()))
                .thenReturn(user);

        when(userMapper.convertToDto(any()))
                .thenReturn(userUpd);

        mockMvc.perform(patch("/users/1")
                        .content("{\"email\":\"updated@user.com\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user")))
                .andExpect(jsonPath("$.email", is("updated@user.com")));
    }

    @Test
    public void whenDeleteUser_thenReturnStatusOK() throws Exception {

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}
