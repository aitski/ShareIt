package ru.yandex.practicum.ShareIt.UserTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class UserServiceIntegrTest {

    @Autowired
    private UserService userService;

    @Test
    public void whenCreateNewUser_thenReturnNewUserWithId() {

        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");

        Assertions.assertEquals(1,userService.create(user).getId());
    }

    @Test
    public void whenGetAll_thenReturnListOfUsers() {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@user.com");
        Assertions.assertEquals(List.of(user),userService.getAll());
    }
}
