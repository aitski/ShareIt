package ru.yandex.practicum.ShareIt.UserTests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.ShareIt.user.UserMapper;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.model.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperJUnitTest {

    UserMapper userMapper = new UserMapper();

    UserDto userDto = new UserDto(1, "user", "user@mail.ru");
    User user = new User();

    @Test
    public void whenConvertToDto_thenReturnDto() {

        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        assertEquals(1, userMapper.convertToDto(user).getId());
        assertEquals("user", userMapper.convertToDto(user).getName());
        assertEquals("user@mail.ru", userMapper.convertToDto(user).getEmail());
    }

    @Test
    public void whenConvertFromDto_thenReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        assertEquals(
                user,
                userMapper.convertFromDto(user,userDto));
    }

}
